package seng202.team1.gui.core;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.screens.*;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Consumer;

import static seng202.team1.gui.GraphicsCommon.WINDOW_HEIGHT;
import static seng202.team1.gui.GraphicsCommon.WINDOW_WIDTH;

public class ScreenManager {

    private static final Logger log = LogManager.getLogger(UIImage.class);

    private Stage currentStage;
    private ScreenType currentScreen;
    private Stack<ScreenType> history = new Stack<>();

    private final List<Consumer<ScreenType>> screenChangeCallbacks = new ArrayList<>();
    private final Map<ScreenType, LayoutElement> screens = new HashMap<>();

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
        loadScreen(ScreenType.DASHBOARD, new DashboardScreen(this));
        loadScreen(ScreenType.MAP, new MapScreen(this));
        loadScreen(ScreenType.LEADERBOARD, new LeaderboardScreen(this));
        loadScreen(ScreenType.TIMELINE, new TimelineScreen(this));
        loadScreen(ScreenType.SETTINGS, new SettingsScreen(this));
        loadScreen(ScreenType.DATA, new DataScreen(this));
    }

    private void loadScreen(ScreenType type, LayoutElement screen){

        // Should never have two of the same screen types
        if(screens.get(type) != null)
            throw new InvalidParameterException("Screen type already exists");

        screens.put(type, screen);
    }

    public void showScreen(ScreenType screenType) {

        // Update screens to show latest data pre render
        for (Consumer<ScreenType> callback : screenChangeCallbacks)
            callback.accept(screenType);

        // Select the screen
        currentScreen = screenType;
        LayoutElement screen = screens.get(screenType);

        // Screen not stored
        if(screen == null){
            log.warn("Screen not loaded for type: {}", screenType.name());
            return;
        }

        // Show the screen
        history.add(currentScreen);
        layout.swap(screen.getRendered());
    }

    public ScreenType getCurrentScreen() {
        return currentScreen;
    }

    public boolean registerSceneChangeCallback(Consumer<ScreenType> callback){

        // Prevent a callback being triggered twice
        if(screenChangeCallbacks.contains(callback))
            return false;

        // Store
        screenChangeCallbacks.add(callback);
        return false;
    }
}
