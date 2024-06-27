package boardgame;

import boardgame.UInavigation.StartController;
import javafx.application.Application;

import org.tinylog.Logger;

public class Main {

    public static void main(String[] args) {
        Logger.info("Elindult a program!");
        Application.launch(StartController.class, args);
        Logger.info("Végetért a program!");
    }

}
