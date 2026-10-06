package seng202.team1.tasks.preflight;

import seng202.team1.state.GlobalState;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

/**
 * A task to parse the geojson containing the bin dataset
 */
public class ParseDatasetTask extends Task {

     /**
     * Create a new ParseDatasetTask
     *
     * @param preflightManager The task manager responsible for this task
     */
    public ParseDatasetTask(PreflightManager preflightManager){
        super(preflightManager, "ccc bin dataset parsing");
    }

     /**
     * The job this task carries out
     *
     * @param manager The task manager that triggered the task
     */
    @Override
    public void task(GlobalState state) {
        state.getDataService().getBinDataset().readGeoJSON();
    }
}
