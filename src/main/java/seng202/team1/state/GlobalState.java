package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.tasks.background.SyncToBackendTask;
import seng202.team1.types.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.types.trashdb.TrashDBLeaderboardResponse;
import seng202.team1.database.DAO.StatusReportDAO;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.gui.Router;
import seng202.team1.gui.pages.Pages;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.data.bin.BinDataset;
import seng202.team1.types.bin.Bin;
import seng202.team1.tasks.TaskManager;
import seng202.team1.tasks.preflight.PreflightManager;
import seng202.team1.types.status.StatusReport;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


/**
 * Stores the current state of the app and provides managers to control it
 *
 * @todo bin dataset doesnt need to be loaded each open? move to task
 */
public class GlobalState {

    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// / Whethere the state is for a unittest or not
    public static boolean IS_TEST_MODE = false;

    /// The manager of the app's window
    private Router router;

    /// The current window
    private Stage stage;

    /// The handler for the static bin data
    private BinDataset binDataset;

    /// The DAO responsible for trash
    private TrashLogDAO trashLogDAO;

    /// The DAO responsible for bins
    private BinDAO binDAO;

    /// The DAO responsible for reports
    private StatusReportDAO statusReportDAO;

    /// The database connection to the local sqlite DB
    private DatabaseManager databaseManager;

    /// The handler for geocoding requests
    private PhotonClient photonClient;

    /// The handler for app API requests
    private TrashDBClient taServer;

    /// Current user location or null if none is requested
    private Feature requestedLocation;

    boolean testState = false;
    /// Current bin to show or null if none is requested
    private Integer requestedShowBinId;

    /// The handler for issue reports
    private StatusReportStore statusReportStore;

    /// The handler for app start up tasks
    private PreflightManager preflightManager;

    /// The handler for app background tasks
    public TaskManager backgroundTasks = new TaskManager(this);

    /// The current leaderboard state
    private TrashDBLeaderboardResponse leaderboard;

    /// The current user and credentials
    private AuthState auth;

    /**
     * Construct a new Global state instance
     *
     * @param stage the primary JavaFX stage to hand off to the {@link Router}
     */
    public GlobalState(Stage stage) {

        // Setup data
        databaseManager = new DatabaseManager(IS_TEST_MODE ?
                "jdbc:sqlite:./src/test/resources/test_database.db" :
                "jdbc:sqlite:database.db"
        );
        binDataset = new BinDataset();

        binDAO = new BinDAO(databaseManager);
        trashLogDAO = new TrashLogDAO(databaseManager);
        statusReportDAO = new StatusReportDAO(databaseManager);

        // Set up API
        photonClient = new PhotonClient();
        taServer = new TrashDBClient();
        auth = new AuthState(this);

        // Start the app
        preflightManager = new PreflightManager(this);
        router = new Router(this, stage);

        // Fetch data etc for cached sessions
        if (!IS_TEST_MODE)
            auth.tryTriggerLoadSession();

    }


    /**
     * Fetch the data the app needs in the background
     */
    public void fetchData() {

        taServer.getLeaderboard(leaderboard -> {

            this.leaderboard = leaderboard;
            router.refreshPage(Pages.COMMUNITY);

        });
    }

    /**
     * Set a new map search request for the map to handle
     *
     * @param location The search request
     */
    public void setMapSearchRequest(Feature location) {
        requestedLocation = location;
        router.switchPage(Pages.MAP);
    }

    /**
     * Get the current map search request and clear it
     *
     * @return The search request or null if none
     */
    public Feature consumeMapSearchRequest() {

        // Clear the request
        Feature location = requestedLocation;
        requestedLocation = null;

        return location;
    }

    /**
     * Start a background task to sync data with the backend DB
     */
    public void sync() {

        backgroundTasks.runTask(new SyncToBackendTask(backgroundTasks));

    }

    /**
     * Get the current window manager
     *
     * @return The window manager for the app
     */
    public Router getRouter() {
        return router;
    }

    /**
     * Get the trash log DAO
     *
     * @return The trash log DAO
     */
    public TrashLogDAO getTrashLogDAO() {
        return trashLogDAO;
    }

    /**
     * Get the bin DAO
     *
     * @return The trash log DAO
     */
    public BinDAO getBinDAO() {
        return binDAO;
    }


    /**
     * get the status report DAO
     *
     * @return The status report DAO
     */
    public StatusReportDAO getStatusReportDAO() {
        return statusReportDAO;
    }

    /**
     * Get the geocoding API client
     *
     * @return The geocoding API client
     */
    public PhotonClient getPhotonClient() {
        return photonClient;
    }

    /**
     * Get the backend API client
     *
     * @return The backend API client
     */
    public TrashDBClient getTaServer() {
        return taServer;
    }

    /**
     * Get the connection to the local database
     *
     * @return The local database client
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Get the CCC bin dataset
     *
     * @return The bin dataset
     */
    public BinDataset getDataset() {
        return binDataset;
    }

    /**
     * Get the task manager for startup tasks
     *
     * @return The start up tasks manager
     */
    public PreflightManager getPreflightManager() {
        return preflightManager;
    }

    /**
     * Get the task manager for background tasks
     *
     * @return The background tasks manager
     */
    public TaskManager getBackgroundTasks() {
        return backgroundTasks;
    }

    /**
     * Get the current leaderboard
     *
     * @return The current leaderboard
     */
    public TrashDBLeaderboardResponse getCommunity() {
        return leaderboard;
    }

    /**
     * Get the current auth state
     *
     * @return The auth state
     */
    public AuthState getAuth() {
        return auth;
    }


    public void refreshBinFullness() {

        // No need to recalc fullness every test
        if (IS_TEST_MODE)
            return;

        // Only want recent reports of fullness
        Instant startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        List<StatusReport> fullnessReports = statusReportDAO.getAll().stream()
                                                    .filter(r -> r.getIssue() == ReportType._FULLNESS_UPDATE)
                                                    .filter(r -> r.getSubmittedAt().isAfter(startOfDay))
                                                    .toList();

        // Get each bin's new fullness as an average of this week's reports
        getBinDAO().getAll().stream().forEach(b -> {
            List<StatusReport> reportsFoBin = fullnessReports.stream().filter(r -> r.getBinId() == b.getID()).toList();
            refreshBinFullness(b, binDAO, reportsFoBin);
        });
    }

    public void refreshBinFullness(Bin bin){
        Instant startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
         // Only want recent reports of fullness
        List<StatusReport> reportsFoBin = statusReportDAO.getFullnessReportsByBinId(bin.getID()).stream()
                .filter(r -> r.getSubmittedAt().isAfter(startOfDay))
                .toList();;
        refreshBinFullness(bin, binDAO, reportsFoBin);
    }

    private void refreshBinFullness(Bin bin, BinDAO binDAO, List<StatusReport> reportsFoBin) {
        bin.updateFullness(binDAO, reportsFoBin);
    }


    /**
     * Switch to the map and try show a request bin
     *
     * @param id The bin to show (must be a int)
     */
    public void goToBin(String id) {

        requestedShowBinId = Integer.valueOf(id);
        router.switchPage(Pages.MAP);
    }

    /**
     * Get the bin show request and clear it
     *
     * @return The bin to show or null if none requested
     */
    public Integer consumeBinShowRequest(){

        Integer binID = requestedShowBinId;
        requestedShowBinId = null;
        return binID;
    }
}
