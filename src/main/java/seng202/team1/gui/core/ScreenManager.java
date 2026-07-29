package seng202.team1.gui.core;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng202.team1.gui.screens.ScreenType;

import java.util.Objects;

import static seng202.team1.gui.GraphicsCommon.WINDOW_HEIGHT;
import static seng202.team1.gui.GraphicsCommon.WINDOW_WIDTH;

public class ScreenManager {

    private Stage currentStage;
    private final CoreUI layout = new CoreUI();

    public ScreenManager(Stage currentStage){

        // Load core ui
        this.currentStage = currentStage;
        Scene scene = new Scene(layout.getRendered(), WINDOW_WIDTH, WINDOW_HEIGHT);
        currentStage.setScene(scene);

        // Load CSS
        String cssPath = Objects.requireNonNull(getClass().getResource("/fxcss/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);

        // Show dashboard
        loadScreens();
        showScreen(ScreenType.DASHBOARD);
    }

    private void loadScreens() {
        // todo: load screens
    }

    private void showScreen(ScreenType screenType) {

        // select the screen
        // store history
        // layout -> swap(scr)

    }
}
