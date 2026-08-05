package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.data.DataLoader;
import seng202.team1.gui.core.WindowManager;

public class GlobalState {

    public static boolean IS_HEADLESS_MODE = false;

    private WindowManager windowManager;

    public DataLoader getDataLoader() {
        return dataLoader;
    }

    private DataLoader dataLoader;

    public GlobalState(Stage stage){

        // Data must be loaded first as UI depends on it
        this.dataLoader = new DataLoader();
        dataLoader.readGeoJSON();

        this.windowManager = new WindowManager(this, stage);
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }
}
