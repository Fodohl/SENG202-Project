package seng202.team1.tasks.preflight;

import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class ReadStatusReportsTask extends Task {

    public ReadStatusReportsTask(TaskManager taskManager){
        super(taskManager, "status reports parsing");
    }

    @Override
    public void task(TaskManager manager) {

          if(!manager.getGlobalState().isTestState())
            manager.getGlobalState().getStatusReportStore().load();
    }
}
