package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.api.trashdb.TrashDBLeaderboardResponse;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.data.trashlog.TrashLogStore;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.pages.Pages;

/**
 * Stores the current state of the app and provides managers to control it
 */
public class GlobalState {

    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// The manager of the app's window
    private WindowManager windowManager;

    /// The current window
    private Stage stage;

    /// The handler for the static bin data
    private BinLoader binLoader;

    /// The handler for trash logs
    private TrashLogStore trashLogStore;

    /// The handler for issue reports
    private StatusReportStore statusReportStore;

    /// The handler for geocoding API requests
    private PhotonClient photonClient;

    /// The handler for app API requests
    private TrashDBClient trashDBClient;

    /// The current pending location request, or null if none pending
    private Feature requestedLocation;

    /// The current leaderboard state
    private TrashDBLeaderboardResponse leaderboard;

    /**
     * Constructs a new GlobalState and sets up managers
     *
     * @param stage The window for the app
     */
    public GlobalState(Stage stage) {

        // Data must be loaded first as UI depends on it
        binLoader = new BinLoader();
        binLoader.readGeoJSON();

        trashLogStore = new TrashLogStore(TrashLogStore.defaultDataPath());
        trashLogStore.load();

        //@todo clean up loaders & paths
        statusReportStore = new StatusReportStore(binLoader, StatusReportStore.defaultDataPath());
        statusReportStore.load();
        binLoader.loadBinFullness(statusReportStore);

        photonClient = new PhotonClient();
        trashDBClient = new TrashDBClient();

        this.stage = stage;
        windowManager = new WindowManager(this, stage);

        fetchData();
    }

    /**
     * Fetch the data the app needs in the background
     */
    private void fetchData() {

        trashDBClient.getLeaderboard(leaderboard -> {

            this.leaderboard = leaderboard;
            windowManager.refreshPage(Pages.LEADERBOARD);

        });
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
     * Get the bin data loader
     *
     * @return The bin loader for the app
     */
    public BinLoader getBinLoader() {
        return binLoader;
    }


    /**
     * Get the trash log handler
     *
     * @return The trash log handler
     */
    public TrashLogStore getTrashLogStore() {
        return trashLogStore;
    }

    /**
     * Get the status log handler
     *
     * @return The status log handler
     */
    public StatusReportStore getStatusReportStore() {
        return statusReportStore;
    }

    /**
     * Get the geocoding API handler
     *
     * @return The geocoding API handler
     */
    public PhotonClient getPhotonClient() {
        return photonClient;
    }

    /**
     * Get the current leaderboard
     *
     * @return The current leaderboard
     */
    public TrashDBLeaderboardResponse getLeaderboard() {
        return leaderboard;
    }

    /**
     * Set a pending map search request
     *
     * @param location The location searched
     */
    public void setMapSearchRequest(Feature location) {
        requestedLocation = location;
        windowManager.switchPage(Pages.MAP);
    }

    /**
     * Get and clear a pending search request
     *
     * @return The search request or null if none currently requested
     */
    public Feature consumeMapSearchRequest() {

        // Clear the request
        Feature location = requestedLocation;
        requestedLocation = null;

        return location;
    }

    /**
     * Spawn the main app window
     */
    public void show() {
        stage.show();
    }
}
