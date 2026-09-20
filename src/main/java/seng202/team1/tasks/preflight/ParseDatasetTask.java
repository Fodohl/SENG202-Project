package seng202.team1.tasks.preflight;

import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class ParseDatasetTask extends Task {

    public ParseDatasetTask(PreflightManager preflightManager){
        super(preflightManager, "ccc bin dataset parsing");
    }

    @Override
    public void task(TaskManager manager) {
        manager.getGlobalState().getDataset().readGeoJSON();
    }
}
