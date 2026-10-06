package seng202.team1.state;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.api.AppClients;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.data.DataService;
import seng202.team1.database.TableCollection;
import seng202.team1.gui.UIContext;
import seng202.team1.tasks.AppTasks;
import seng202.team1.tasks.background.SyncToBackendTask;
import seng202.team1.tasks.workers.BinFullnessWorker;
import seng202.team1.tasks.workers.CommunityCleanlinessWorker;
import seng202.team1.tasks.workers.LeaderboardWorker;
import seng202.team1.types.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.types.trashdb.CommunityCleanliness;
import seng202.team1.types.trashdb.TrashDBCleanlinessResponse;
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

    private static final Logger log = LoggerFactory.getLogger(GlobalState.class);
    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// Whether the state is for a unittest or not
    public static boolean IS_TEST_MODE = false;

    /// The collection of table DAO objects for the local sqlite DB
    private TableCollection localTables;

    /// The UI builder and state
    private UIContext uiContext;

    /// The current user and credentials
    private AuthState auth;

    /// The handler for data loading and logging
    private DataService dataService;

    /// The task managers the app uses
    private AppTasks appTasks;

    /// The api clients the app uses
    private AppClients appClients;

    /**
     * Construct a new Global state instance
     *
     * @param stage the primary JavaFX stage to hand off to the {@link Router}
     */
    public GlobalState(Stage stage) {

        // Setup connection to sqlite
        DatabaseManager databaseManager = new DatabaseManager(IS_TEST_MODE ?
                "jdbc:sqlite:./src/test/resources/test_database.db" :
                "jdbc:sqlite:database.db"
        );

        localTables = new TableCollection(databaseManager);
        dataService = new DataService(this);
        appClients = new AppClients(this);
        appTasks = new AppTasks(this);
        uiContext = new UIContext(this, stage);
        auth = new AuthState(this);

    }

    /**
     * Start a background task to sync data with the backend DB
     */
    public void sync() {

        appTasks.runBackgroundSync();

    }

    /**
     * Get the current window manager
     *
     * @return The window manager for the app
     */
    public Router getRouter() {
        return uiContext.getRouter();
    }

    /**
     * Get the current auth state
     *
     * @return The auth state
     */
    public AuthState getAuth() {
        return auth;
    }

    public TableCollection getLocalTables() {
        return localTables;
    }

    public UIContext getUiContext() {
        return uiContext;
    }

    public AppTasks getAppTasks() {
        return appTasks;
    }

    public AppClients getAppClients() {
        return appClients;
    }

    public DataService getDataService() {
        return dataService;
    }
}
