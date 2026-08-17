package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.data.DataLoader;
import seng202.team1.gui.WindowManager;

/**
 * Stores the current state of the app and provides managers to control it
 */
public class GlobalState {

    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// The manager of the app's window
    private WindowManager windowManager;

    /// The handler for the static bin data
    private DataLoader dataLoader;

    /**
     * Constructs a new GlobalState and sets up managers
     *
     * @param stage The window for the app
     */
    public GlobalState(Stage stage){

        // Data must be loaded first as UI depends on it
        this.dataLoader = new DataLoader();
        dataLoader.readGeoJSON();

        this.windowManager = new WindowManager(this, stage);
    }

    /**
     * Get the current window manager
     *
     * @return The window manager for the app
     */
    public WindowManager getWindowManager() {
        return windowManager;
    }

    /**
     * Get the current data loader
     *
     * @return The data loader for the app
     */
    public DataLoader getDataLoader() {
        return dataLoader;
    }

}
