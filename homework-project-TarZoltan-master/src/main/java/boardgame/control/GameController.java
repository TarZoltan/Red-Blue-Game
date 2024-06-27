package boardgame.control;

import boardgame.model.GameModel;
import boardgame.model.Position;
import boardgame.model.Disk;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;

import static boardgame.control.MoveSelector.Phase;

/**
 * A játék irányítását végző osztály.
 *
 */
public class GameController {

    @FXML
    private GridPane board;

    private static Stage stage;


    private static GameModel model = new GameModel();

    private MoveSelector selector = new MoveSelector(model);

    /**
     * Ez a metódus vet véget a játéknak amikor annak eljön az ideje.
     * Eredményt hirdet, majd bezárja az alkalmazást
     *
     */
    public static void GameOver() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Over");
        Logger.info("Vége a játéknak!");
        if (GameModel.PLAYER == -1){
            alert.setContentText("Gartulálunk, " + GameModel.getBlue_Player() + "! Győztél");

        }else {
            alert.setContentText("Gartulálunk, " + GameModel.getRed_Player() + "! Győztél");
        }
        alert.showAndWait();
        System.exit(0);
    }




    /**
     * Ez a metódus inicializálja a táblát, és "megjelöli" a tiltott mezőt, stílussal látja el.
     *
     */
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var disk = createDisk(i, j);
                board.add(disk, j, i);
                if((i == 3 && j ==2) || (i == 2 && j == 4)){
                    disk.getStyleClass().add("black");
                    Logger.info("Tiltott mező megjelölése");
                }
            }
        }
        selector.phaseProperty().addListener(this::showSelectionPhaseChange);
        Logger.info("Tábla inicializálva");
    }

    private StackPane createDisk(int i, int j) {
        var disk = new StackPane();
        disk.getStyleClass().add("disk");
        var piece = new Circle(25);
        piece.fillProperty().bind(createDiskBinding(model.diskProperty(i, j)));
        disk.getChildren().add(piece);
        disk.setOnMouseClicked(this::handleMouseClick);
        return disk;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var disk = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(disk);
        var col = GridPane.getColumnIndex(disk);
        selector.select(new Position(row, col));
        if (selector.isReadyToMove()) {
            selector.makeMove();
        }
    }

    private ObjectBinding<Paint> createDiskBinding(ReadOnlyObjectProperty<Disk> diskProperty) {
        return new ObjectBinding<Paint>() {
            {
                super.bind(diskProperty);
            }
            @Override
            protected Paint computeValue() {
                return switch (diskProperty.get()) {
                    case NONE -> Color.TRANSPARENT;
                    case RED -> Color.RED;
                    case BLUE -> Color.BLUE;
                    case BLACK -> Color.TRANSPARENT;
                };
            }
        };
    }

    private void showSelectionPhaseChange(ObservableValue<? extends Phase> value, Phase oldPhase, Phase newPhase) {
        switch (newPhase) {
            case SELECT_FROM -> {}
            case SELECT_TO -> showSelection(selector.getFrom());
            case READY_TO_MOVE -> hideSelection(selector.getFrom());
        }
    }

    private void showSelection(Position position) {
        var disk = getDisk(position);
        disk.getStyleClass().add("selected");
        Logger.info("Kiválasztva: ({},{})", position.row(), position.col());
    }

    private void hideSelection(Position position) {
        var disk = getDisk(position);
        disk.getStyleClass().remove("selected");
        Logger.info("Lépés!");
    }

    private StackPane getDisk(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }


}
