package boardgame.UInavigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;
import java.io.IOException;

/**
 * A játék menü felületét vezérlő osztály.
 *
 */
public class MenuController {

    @FXML
    private TextField playerRedTextField;

    @FXML
    private TextField playerBlueTextField;

    @FXML
    private Label errorLabel;

    /**
     * A metódus a megfelelő esemény bekövetkeztével átlép a játék jelenetére, amennyiben a játlkosok megadták a neveiket.
     *
     * @param event a menü felületen történő "Game" gomb megnyomása
     */
    @FXML
    private void switchToGame(ActionEvent event) throws IOException {
        if (playerRedTextField.getText().isBlank() || playerBlueTextField.getText().isBlank()) {
            errorLabel.setText("Add meg a neved!");
            Logger.info("Nem adott meg neveket!");
        } else {
            boardgame.model.GameModel.setBlue_Player(playerBlueTextField.getText());
            boardgame.model.GameModel.setRed_Player(playerRedTextField.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/game.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("Váltás a játék képernyőre!");
        }
    }


    /**
     * A metódus a megfelelő esemény bekövetkezte után átléptet a "result" jelenetbe.
     *
     * @param event a menü felületen történő "Result" gomb megnyomása
     */
    @FXML
    private void switchToResult(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/table.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.info("Váltás a Result képernyőre!");
    }
}
