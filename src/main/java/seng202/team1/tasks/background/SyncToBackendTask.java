package seng202.team1.tasks.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.api.trashdb.TrashDBBackend;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;
import seng202.team1.types.bin.Bin;
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

    /// The server to upload the trash logs onto
    TrashDBClient server;

    /**
     * Create a new SyncToBackendTask
     *
     * @param taskManager The task manage responsible for this task
     */
    public SyncToBackendTask(TaskManager taskManager){
        super(taskManager, "data syncing");

        logDAO = taskManager.getGlobalState().getTrashLogDAO();
        server = taskManager.getGlobalState().getTaServer();

    }

    /**
     * The job this task carries out
     *
     * @param manager The task manager that triggered the task
     */
    @Override
    public void task(TaskManager manager) {

        // Wait for any other sync tasks to complete as may pick up what this one is doing
        lock.lock();
        try{

            // Get items needing to be synced
            List<TrashLog> logs = logDAO.getUnsynced();
            log.info(String.format("Syncing %d logs", logs.size()));

            for(TrashLog log : logs){
                server.postLog(log, r-> {}).join();
                logDAO.update(log.asSynced());
            }


        } finally {
            lock.unlock();
        }
    }
}
