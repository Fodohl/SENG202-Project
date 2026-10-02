package seng202.team1.tasks.preflight;

import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PreflightManager extends TaskManager {

    public PreflightManager(GlobalState state){
        super(state);

        // Start preflight tasks
        long parseDataSetTask = runTask(new ParseDatasetTask(this));
        long propegateBinTask = runTask(new PropegateBinsTask(this), parseDataSetTask);
        long readSatusReports = runTask(new ReadStatusReportsTask(this));
        long refreshBinsFulls = runTask(new RefreshBinFullnessTask(this), readSatusReports, propegateBinTask);
    }
}
