package seng202.team1.tasks.preflight;

import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

/**
 * A task to load all the logged status reports
 */
public class ReadStatusReportsTask extends Task {

    /**
     * Create a new ReadStatusReportsTask
     *
     * @param taskManager The task manager responsible for this task
     */
    public ReadStatusReportsTask(TaskManager taskManager) {
        super(taskManager, "status reports parsing");
    }

    /**
     * The job this task carries out
     *
     * @param manager The task manager that triggered the task
     */
    @Override
    public void task(TaskManager manager) {

        if (!manager.getGlobalState().isTestState())
            manager.getGlobalState().getStatusReportStore().load();
    }
}
