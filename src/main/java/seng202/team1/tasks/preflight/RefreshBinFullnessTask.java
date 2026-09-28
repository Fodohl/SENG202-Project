package seng202.team1.tasks.preflight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.taserver.model.GetBinsFullness200Response;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;
import seng202.team1.types.bin.BinFullness;

import java.util.HashMap;

public class RefreshBinFullnessTask extends Task {

    /// RefreshBinFullnessTask specific logger
    private static final Logger log = LoggerFactory.getLogger(RefreshBinFullnessTask.class);

    public RefreshBinFullnessTask(TaskManager taskManager){
        super(taskManager, "bin fullness calculations");
    }

    @Override
    public void task(TaskManager manager) {

        GetBinsFullness200Response bins = manager.getGlobalState().getTaServer().getBinFullness(r -> {}).join();
        HashMap<Integer, BinFullness> fullness = new HashMap<>();

        log.info("Refreshed {} bins", bins.getBins().size());

        // Convert to known format
        bins.getBins().forEach(bin -> {
            fullness.put(bin.getId().intValue(), BinFullness.values()[bin.getFullness().intValue()]);
        });

        manager.getGlobalState().getBinDAO().updateFullness(fullness);
    }
}
