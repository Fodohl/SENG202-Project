package seng202.team1.tasks.preflight;

import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class CheckBackendTask extends Task {

    public CheckBackendTask(PreflightManager manager) {
        super(manager, "backend availability check");
    }

    @Override
    public void task(TaskManager manager) {
    }
}
