package seng202.team1.tasks.preflight;

import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class RefreshBinFullnessTask extends Task {

    public RefreshBinFullnessTask(TaskManager taskManager){
        super(taskManager, "bin fullness calculations");
    }

    @Override
    public void task(TaskManager manager) {
        manager.getGlobalState().refreshBinFullness();
    }
}
