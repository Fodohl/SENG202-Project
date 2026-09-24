package seng202.team1.tasks.preflight;

import seng202.team1.taserver.model.Heartbeat200Response;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class CheckBackendTask extends Task {

    public CheckBackendTask(PreflightManager manager) {
        super(manager, "backend availability check");
    }

    @Override
    public void task(TaskManager manager) {
        try {

            // No need to send async as already in background
            Heartbeat200Response res = manager.getGlobalState().getTaServer().getHeartbeat(r -> {}).join();
            System.out.println(res.getMotd());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
