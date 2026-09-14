package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.data.trashlog.TrashLogStore;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.pages.Pages;

import java.util.Locale;
import java.util.Map;

/**
 * Stores the current state of the app and provides managers to control it
 */
public class GlobalState {

    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// The manager of the app's window
    private WindowManager windowManager;

    private Stage stage;

    /// The handler for the static bin data
    private BinLoader binLoader;

    private TrashLogStore trashLogStore;

    private StatusReportStore statusReportStore;

    private PhotonClient photonClient;

    private Feature requestedLocation;

    /**
     * Constructs a new GlobalState and sets up managers
     *
     * @param stage The window for the app
     */
    public GlobalState(Stage stage){

        // Data must be loaded first as UI depends on it
        binLoader = new BinLoader();
        binLoader.readGeoJSON();

        trashLogStore = new TrashLogStore(TrashLogStore.defaultDataPath());
        trashLogStore.load();

        //@todo clean up loaders & paths
        statusReportStore = new StatusReportStore(binLoader, StatusReportStore.defaultDataPath());
        statusReportStore.load();

        photonClient = new PhotonClient();

        this.stage = stage;
        windowManager = new WindowManager(this, stage);
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
    public BinLoader getDataLoader() {
        return binLoader;
    }

    public TrashLogStore getTrashLogStore() {
        return trashLogStore;
    }

    public PhotonClient getPhotonClient() {
        return photonClient;
    }

    public void setMapSearchRequest(Feature location) {
        requestedLocation = location;
        windowManager.switchPage(Pages.MAP);
    }

    public Feature consumeMapSearchRequest(){

        // Clear the request
        Feature location = requestedLocation;
        requestedLocation = null;

        return location;
    }

    public void show() {
        stage.show();
    }
}
