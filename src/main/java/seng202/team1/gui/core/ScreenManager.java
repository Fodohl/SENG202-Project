package seng202.team1.gui.core;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng202.team1.gui.screens.ScreenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static seng202.team1.gui.GraphicsCommon.WINDOW_HEIGHT;
import static seng202.team1.gui.GraphicsCommon.WINDOW_WIDTH;

public class ScreenManager {

    private Stage currentStage;
    private ScreenType currentScreen;

    private final List<Consumer<ScreenType>> sceneChangeCallbacks = new ArrayList<>();

    private final CoreUI layout = new CoreUI(this);

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

    public void showScreen(ScreenType screenType) {

        // Update screens to show latest data pre render
        for (Consumer<ScreenType> callback : sceneChangeCallbacks)
            callback.accept(screenType);

        // Select the screen
        currentScreen = screenType;
        System.out.println(screenType);
        // cache.get(..)

        // store history
        // layout -> swap(scr)


    }

    public ScreenType getCurrentScreen() {
        return currentScreen;
    }

    public boolean registerSceneChangeCallback(Consumer<ScreenType> callback){

        // Prevent a callback being triggered
        if(sceneChangeCallbacks.contains(callback))
            return false;

        // Store
        sceneChangeCallbacks.add(callback);
        return false;
    }
}
