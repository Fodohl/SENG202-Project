package seng202.team1.tasks.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.api.trashdb.TrashDBBackend;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.database.DAO.StatusReportDAO;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.database.TableCollection;
import seng202.team1.state.GlobalState;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A task to upload trash logs and issue reports to the database
 */
public class SyncToBackendTask extends Task {

    /// Task specific logger
    private static final Logger log = LoggerFactory.getLogger(SyncToBackendTask.class);

    /// Lock to prevent concurrent uploads of the same items
    ReentrantLock lock = new ReentrantLock();

    /// The reference to the local trash logs
    TrashLogDAO logDAO;

    /// The reference to the local trash logs
    StatusReportDAO statusReportDAO;

    /// The server to upload the trash logs onto
    TrashDBClient server;

    /**
     * Create a new SyncToBackendTask
     *
     * @param taskManager The task manage responsible for this task
     */
    public SyncToBackendTask(TaskManager taskManager){
        super(taskManager, "data syncing");

        TableCollection tables = taskManager.getGlobalState().getLocalTables();
        logDAO = tables.getTrashTable();
        statusReportDAO = tables.getStatusReportsTable();
        server = taskManager.getGlobalState().getAppClients().getTaServer();

    }

    /**
     * The job this task carries out
     *
     */
    @Override
    public void task(GlobalState state) {

        // Wait for any other sync tasks to complete as may pick up what this one is doing
        lock.lock();
        try{

            // Get items needing to be synced
            List<TrashLog> logs = logDAO.getUnsynced();
            List<StatusReport> reports = statusReportDAO.getUnsynced();
            log.info(String.format("Syncing %d logs, %d reports", logs.size(), reports.size()));

            for(TrashLog log : logs){
                server.postLog(log, r-> {}).join();
                logDAO.update(log.asSynced());
            }

            for(StatusReport report : reports){
                server.postReport(report, r-> {}).join();
                statusReportDAO.update(report.asSynced());
            }


        } finally {
            lock.unlock();
        }
    }
}
