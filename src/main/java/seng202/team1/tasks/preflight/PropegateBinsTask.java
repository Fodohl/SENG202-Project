package seng202.team1.tasks.preflight;

import seng202.team1.data.bin.BinDataset;
import seng202.team1.data.sqlite.DAO.BinDAO;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class PropegateBinsTask extends Task {

    public PropegateBinsTask(TaskManager manager){
        super(manager, "initial bin dataset caching");
    }

    @Override
    public void task(TaskManager manager) {

        BinDAO binDB = manager.getGlobalState().getBinDAO();
        BinDataset binDataset = manager.getGlobalState().getDataset();

        // Bins already propagated
        if(!binDB.getAll().isEmpty())
            return;

        // Add all bins
        binDB.addBatch(binDataset.getBinDataset());
    }
}
