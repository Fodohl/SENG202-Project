package seng202.team1;

import javafx.application.Application;
import javafx.stage.Stage;
import seng202.team1.gui.core.ScreenManager;

import static javafx.application.Application.launch;

public class Main extends Application {

     /**
     * The JavaFX entry point to the game. Will setup the window and show the main menu
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage stage) {

        // Set up the window
        stage.setTitle("TrashApp v1");

        // Setup app state
        ScreenManager screenManager = new ScreenManager(stage);
        stage.show();
    }


    /**
     * Entry point to the application. Launches the JavaFX runtime
     *
     * @param args Command line arguments, unused
     */
    public static void main(String[] args) {
        launch();
    }

}
