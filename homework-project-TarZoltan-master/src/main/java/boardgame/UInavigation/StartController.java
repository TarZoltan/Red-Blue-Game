package boardgame.UInavigation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

/**
 * A program indulását vezérlő osztály.
 *
 */
public class StartController extends Application {

    /**
     *Induláskor elnavigál minket a "menu" képernyőre.
     *
     * @param stage az elsődleges szinpad, ezen jelennek meg az alkalmazás jelenetei
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        stage.setTitle("StepGame");
        stage.setScene(new Scene(root));
        stage.show();
        Logger.info("Menu betöltve!");

    }

}
