package seng202.team1.tasks.preflight;

import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;
import seng202.team1.tasks.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PreflightManager extends TaskManager {


    public PreflightManager(GlobalState state){
        super(state);

        // Start preflight tasks
        runTask(new CheckBackendTask(this));
        runTask(new PropegateBinsTask(this));

    }
}
