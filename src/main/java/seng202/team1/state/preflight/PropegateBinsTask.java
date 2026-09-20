package seng202.team1.state.preflight;

import seng202.team1.data.bin.BinDataset;
import seng202.team1.data.sqlite.DAO.BinDAO;

public class PropegateBinsTask extends PreflightTask{

    public PropegateBinsTask(PreflightManager manager){
        super(manager, "initial bin dataset caching");
    }

    @Override
    public void task(PreflightManager manager) {

        BinDAO binDB = manager.getGlobalState().getBinDAO();
        BinDataset binDataset = manager.getGlobalState().getDataset();

        // Bins already propagated
        if(!binDB.getAll().isEmpty())
            return;

        // Add all bins
        binDB.addBatch(binDataset.getBinDataset());
    }
}
