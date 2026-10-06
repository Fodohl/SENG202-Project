package seng202.team1.tasks.workers;

import seng202.team1.state.GlobalState;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class BinFullnessWorker extends Task {

    public BinFullnessWorker(TaskManager taskManager){
        super(taskManager, "bin fullness calculations");
    }


    @Override
    public void task(GlobalState state) {
        state.getAppClients().getTaServer().runRecomputeFullness();
    }
}
