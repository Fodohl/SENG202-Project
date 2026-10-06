package seng202.team1.tasks.workers;

import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

public class LeaderboardWorker extends Task {

    public LeaderboardWorker(TaskManager taskManager){
        super(taskManager, "leaderboard position calculations");
    }


    @Override
    public void task(TaskManager manager) {
        manager.getGlobalState().getTaServer().runRecomputeLeaderboard();
    }
}
