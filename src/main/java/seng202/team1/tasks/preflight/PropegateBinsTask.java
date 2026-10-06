package seng202.team1.tasks.preflight;

import seng202.team1.data.bin.BinDataset;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.state.GlobalState;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

/**
 * A task to load all the bins in the json onto the sqlite db
 */
public class PropegateBinsTask extends Task {

    /**
     * Create a new PropegateBinsTask
     *
     * @param manager The task manager responsible for this task
     */
    public PropegateBinsTask(TaskManager manager) {
        super(manager, "initial bin dataset caching");
    }

    /**
     * The job this task carries out
     *
     * @param manager The task manager that triggered the task
     */
    @Override
    public void task(GlobalState state) {

        BinDAO binDB = state.getLocalTables().getBinTable();
        BinDataset binDataset = state.getDataService().getBinDataset();

        // Bins already propagated
        if (!binDB.getAll().isEmpty())
            return;

        // Add all bins
        binDB.addBatch(binDataset.getBinDataset());
    }
}
