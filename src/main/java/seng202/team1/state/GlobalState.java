package seng202.team1.state;

import javafx.stage.Stage;
import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.data.sqlite.DAO.TrashLogDAO;
import seng202.team1.gui.Router;
import seng202.team1.gui.Router;
import seng202.team1.gui.pages.Pages;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.sqlite.DAO.BinDAO;
import seng202.team1.data.sqlite.exceptions.InstanceAlreadyExistsException;
import seng202.team1.data.bin.BinDataset;
import seng202.team1.data.bin.Bin;

import java.time.LocalDate;
import java.util.List;


/**
 * Stores the current state of the app and provides managers to control it
 */
public class GlobalState {


    /// Whether there is a screen to draw to or now
    public static boolean IS_HEADLESS_MODE = false;

    /// The manager of the app's window
    private Router router;

    /// The current window
    private Stage stage;

    /// The handler for the static bin data
    private BinDataset binDataset;

    private TrashLogDAO trashLogDAO;

    private BinDAO binDAO;

    private DatabaseManager databaseManager;

    private PhotonClient photonClient;

    private Feature requestedLocation;

    boolean testState = false;

    /// The handler for issue reports
    private StatusReportStore statusReportStore;

    /**
     * Construct a new Global state instance
     *
     * @param stage the primary JavaFX stage to hand off to the {@link WindowManager}
     */
    public GlobalState(Stage stage, boolean testState) {

        this.testState = testState;
        databaseManager = new DatabaseManager(testState ?
                                                "jdbc:sqlite:./src/test/resources/test_database.db" :
                                                "jdbc:sqlite:database.db"
                                            );
        binDataset = new BinDataset();
        binDataset.readGeoJSON();

        binDAO = new BinDAO(databaseManager);
        trashLogDAO = new TrashLogDAO(databaseManager);

        statusReportStore = new StatusReportStore(binDAO, testState ? StatusReportStore.testDataPath()
                                                                    : StatusReportStore.defaultDataPath());
        if(!testState)
            statusReportStore.load();

        loadBins();
        refreshBinFullness();
        router = new Router(this, stage);

        photonClient = new PhotonClient();
    }

    /**
     * Get the current window manager
     *
     * @return The window manager for the app
     */
    public Router getRouter() {
        return router;
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
        router.switchPage(Pages.MAP);
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
        if (binDAO.getAll().isEmpty())
            binDAO.addBatch(binDataset.getBinDataset());
    }


    public void refreshBinFullness(){

        // No need to recalc fullness every test
        if(testState)
            return;

        // Only want recent reports of fullness
        List<StatusReport> fullnessReports = statusReportStore.getReports().stream()
                                                    .filter(r -> r.getIssue() == ReportType._FULLNESS_UPDATE)
                                                    .filter(r -> r.getSubmittedAt().isAfter(LocalDate.now().atStartOfDay()))
                                                    .toList();

        // Get each bin's new fullness as an average of this week's reports
        getBinDAO().getAll().stream().forEach(b -> {
            List<StatusReport> reportsFoBin = fullnessReports.stream().filter(r -> r.getBinID() == b.getID()).toList();
            refreshBinFullness(b, binDAO, reportsFoBin);
        });
    }

    public void refreshBinFullness(Bin bin){

         // Only want recent reports of fullness
        List<StatusReport> reportsFoBin = statusReportStore.getReports().stream()
                                                    .filter(r -> r.getIssue() == ReportType._FULLNESS_UPDATE)
                                                    .filter(r -> r.getSubmittedAt().isAfter(LocalDate.now().atStartOfDay()))
                                                    .filter(r -> r.getBinID() == bin.getID())
                                                    .toList();

        refreshBinFullness(bin, binDAO, reportsFoBin);
    }

    private void refreshBinFullness(Bin bin, BinDAO binDAO, List<StatusReport> reportsFoBin) {
        bin.updateFullness(binDAO, reportsFoBin);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public BinDataset getDataset() {
        return binDataset;
    }
}
