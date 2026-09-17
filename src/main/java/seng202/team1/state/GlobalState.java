package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.api.trashdb.TrashDBLeaderboardResponse;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.pages.Pages;
import seng202.team1.data.repository.DatabaseManager;
import seng202.team1.data.repository.TrashLogDAO;
import seng202.team1.data.repository.BinDAO;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.data.bin.BinLoader;


/**
 * Stores the current state of the app and provides managers to control it
 */
public class    GlobalState {

    private static GlobalState instance;

    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// The manager of the app's window
    private WindowManager windowManager;

    /// The current window
    private Stage stage;

    /// The handler for the static bin data
    private BinLoader binLoader;

    private TrashLogDAO trashLogDAO;

    private BinDAO binDAO;

    private DatabaseManager databaseManager;

    private PhotonClient photonClient;

    private TrashDBClient trashDBClient;

    private Feature requestedLocation;

    private TrashDBLeaderboardResponse leaderboard;

    /// The handler for issue reports
    private StatusReportStore statusReportStore;

    private GlobalState() {
    }

    /**
     * Factory method that creates and fully initialises the singleton {@code GlobalState} instance.
     * Called once per application run
     *
     * @param stage the primary JavaFX stage to hand off to the {@link WindowManager}
     * @return the newly created singleton {@code GlobalState} instance
     * @throws InstanceAlreadyExistsException if {@code create} has already been called in this
     *                                        application run
     */
    public static GlobalState create(Stage stage) throws InstanceAlreadyExistsException {
        if (instance != null) {
            throw new InstanceAlreadyExistsException("GlobalState already initialized");
        }

        GlobalState state = new GlobalState();

        state.databaseManager = DatabaseManager.getInstance();

        state.binLoader = new BinLoader();
        state.binLoader.readGeoJSON();

        state.binDAO = new BinDAO();
        state.trashLogDAO = new TrashLogDAO();

        state.statusReportStore = new StatusReportStore(state.binLoader, StatusReportStore.defaultDataPath());
        state.statusReportStore.load();
        state.binLoader.loadBinFullness(state.statusReportStore);

        state.loadBins();

        state.photonClient = new PhotonClient();
        state.trashDBClient = new TrashDBClient();

        state.windowManager = new WindowManager(state, stage);

        state.fetchData();

        instance = state;
        return state;
    }

    /**
     * Returns the singleton {@code GlobalState} instance.
     * @return the singleton {@code GlobalState} instance
     * @throws IllegalStateException if {@link #create(Stage)} has not yet been called
     */
    public static GlobalState getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GlobalState not initialized — call create() first");
        }
        return instance;
    }


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

    public TrashLogDAO getTrashLogDAO() {
        return trashLogDAO;
    }

    public BinDAO getBinDAO() { return binDAO; }

    public PhotonClient getPhotonClient() {
        return photonClient;
    }

    public void setMapSearchRequest(Feature location) {
        requestedLocation = location;
        windowManager.switchPage(Pages.MAP);
    }

    public BinLoader getBinLoader() {
        return binLoader;
    }

    public Feature consumeMapSearchRequest(){

        // Clear the request
        Feature location = requestedLocation;
        requestedLocation = null;

        return location;
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
     * Loads the bins from JSON file to the database if there are no bins in the
     * current database
     */
    private void loadBins() {
        if (binDAO.getAll().isEmpty()) {
            binDAO.addBatch(binLoader.getBinObjects());
        }
    }

    public TrashDBLeaderboardResponse getLeaderboard() {
        return leaderboard;
    }

    /**
     * Resets the singleton instance. Intended for test use only.
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }
}
