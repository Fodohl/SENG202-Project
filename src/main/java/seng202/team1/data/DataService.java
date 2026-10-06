package seng202.team1.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.data.bin.BinDataset;
import seng202.team1.database.TableCollection;
import seng202.team1.state.GlobalState;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;
import seng202.team1.types.user.Session;

import java.time.Instant;

import static seng202.team1.data.bin.BinFullnessCalculator.refreshBinFullness;

public class DataService {

    private static final Logger log = LoggerFactory.getLogger(DataService.class);
    private GlobalState state;

    /// The handler for the static bin data
    private BinDataset binDataset;

    private TableCollection localData;

    public DataService(GlobalState state){
        this.state = state;
        this.localData = state.getLocalTables();
        this.binDataset = new BinDataset();
    }

    public void logTrash(Bin bin, TrashType type, double weight, BinFullness fullness){

        // Get the user info if they are logged in
        Session session = state.getAuth().getSession();
        int userID = session == null ? 1 : session.userDetails().id();

        // Log the trash
        Instant createdAt = Instant.now();
        localData.getTrashTable().add(new TrashLog(createdAt, createdAt, userID, bin.getID(), type, weight));

        // Log the fullness
        logReport(bin, fullness, ReportType._FULLNESS_UPDATE, "");

        // Refresh app with new log info
        refreshBinFullness(bin, localData.getBinTable(), localData.getStatusReportsTable());
        state.sync();
    }


    public void logReport(Bin bin, BinFullness fullness, ReportType type, String description){

        // Get the user info if they are logged in
        Session session = state.getAuth().getSession();
        int userID = session == null ? 1 : session.userDetails().id();

         // Log the report
        Instant createdAt = Instant.now();
        localData.getStatusReportsTable().add(new StatusReport(bin.getID(), userID, fullness, type, description, createdAt));

        // Process upload in the backgound
        state.sync();
    }

    public BinDataset getBinDataset() {
        return binDataset;
    }
}
