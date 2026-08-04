package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.data.DataLoader;
import seng202.team1.gui.core.ScreenManager;

public class GlobalState {

    private ScreenManager screenManager;

    public DataLoader getDataLoader() {
        return dataLoader;
    }

    private DataLoader dataLoader;

    public GlobalState(Stage stage){

            this.screenManager = new ScreenManager(this, stage);
            this.dataLoader = new DataLoader();
            dataLoader.readGeoJSON();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }
}
