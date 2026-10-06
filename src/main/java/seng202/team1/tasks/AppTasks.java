package seng202.team1.tasks;

import seng202.team1.state.GlobalState;
import seng202.team1.tasks.background.SyncToBackendTask;
import seng202.team1.tasks.preflight.PreflightManager;
import seng202.team1.tasks.workers.BinFullnessWorker;
import seng202.team1.tasks.workers.CommunityCleanlinessWorker;
import seng202.team1.tasks.workers.LeaderboardWorker;

public class AppTasks {

    /// The handler for app start up tasks
    private PreflightManager preflightManager;

    /// The handler for app background tasks
    public TaskManager backgroundTasks;

    public AppTasks(GlobalState state){
        preflightManager = new PreflightManager(state);
        backgroundTasks = new TaskManager(state);
    }

    /**
     * Run the tasks that would realistically be periodically ran on the backed, this assumes that users are using the app
     * regularly enough that the crowdsourced work is keeping the data fresh
     */
    public void runWorkers() {

         backgroundTasks.runTask(new BinFullnessWorker(backgroundTasks));
         backgroundTasks.runTask(new CommunityCleanlinessWorker(backgroundTasks));
         backgroundTasks.runTask(new LeaderboardWorker(backgroundTasks));
    }

    public PreflightManager getPreflightManager() {
        return preflightManager;
    }

    public TaskManager getBackgroundTasks() {
        return backgroundTasks;
    }

    public void runBackgroundSync() {
        backgroundTasks.runTask(new SyncToBackendTask(backgroundTasks));
    }
}
