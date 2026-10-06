package seng202.team1.tasks.workers;

import seng202.team1.state.GlobalState;
import seng202.team1.tasks.GenericTask;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class LeaderboardWorker extends Task {

    public LeaderboardWorker(TaskManager taskManager){
        super(taskManager, "leaderboard position calculations");
    }


    @Override
    public void task(GlobalState state) {
        state.getAppClients().getTaServer().runRecomputeLeaderboard();
    }
}
