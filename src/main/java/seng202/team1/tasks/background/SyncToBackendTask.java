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

public class SyncToBackendTask extends Task {

    private static final Logger log = LoggerFactory.getLogger(SyncToBackendTask.class);
    ReentrantLock lock = new ReentrantLock();

    TrashLogDAO logDAO;
    TrashDBClient server;


    public SyncToBackendTask(TaskManager taskManager){
        super(taskManager, "data syncing");

        logDAO = taskManager.getGlobalState().getTrashLogDAO();
        server = taskManager.getGlobalState().getTaServer();

    }

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
