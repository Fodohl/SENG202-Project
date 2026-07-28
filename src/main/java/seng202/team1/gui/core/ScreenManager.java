package seng202.team1.gui.core;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScreenManager {

    public ScreenManager(Stage currentStage){
        Scene scene = new Scene(new VBox(), 1700, 900);
        currentStage.setScene(scene);
    }

}
