package seng202.team1.state.preflight;

import seng202.team1.gui.pages.Pages;
import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PreflightManager extends StateReferrer {

    /// The event handlers triggered when switching pages
    private final List<Consumer<PreflightTask>> taskCompletionCallbacks = new ArrayList<>();

    private List<PreflightTask> remainingTasks = new ArrayList<>();

    public PreflightManager(GlobalState state){
        super(state);
    }


    public void runAll() {
        runTask(new CheckBackendTask(this));
        runTask(new PropegateBinsTask(this));
    }

    public void runTask(PreflightTask task){
        remainingTasks.add(task);

        // Thread the task
        Thread thread = new Thread(() -> task.run());
        thread.start();
    }

    public boolean hasTasksUncompleted(){
        return !remainingTasks.isEmpty();
    }

    public List<PreflightTask> getTasksUncompleted(){
        return remainingTasks;
    }

    public void taskFinished(PreflightTask preflightTask) {
        remainingTasks.remove(preflightTask);

        for(Consumer<PreflightTask> callback : taskCompletionCallbacks)
            callback.accept(preflightTask);
    }

     /**
     * Register an event handler for a task completion event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a task is completed
     * @return True if the callback was registered successfully
     */
    public boolean registerTaskCompletionCallback(Consumer<PreflightTask> callback){

        // Prevent a callback being triggered twice
        if(taskCompletionCallbacks.contains(callback))
            return false;

        // Store
        taskCompletionCallbacks.add(callback);
        return true;
    }
}
