package seng202.team1.tasks;

import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TaskManager extends StateReferrer {

    /// The event handlers triggered when switching pages
    private final List<Consumer<Task>> taskCompletionCallbacks = new ArrayList<>();

    private List<Task> remainingTasks = new ArrayList<>();

    public TaskManager(GlobalState state){
        super(state);
    }

    public void runTask(Task task){
        remainingTasks.add(task);

        // Thread the task
        Thread thread = new Thread(() -> task.run());
        thread.start();
    }

    public void runTask(String name, Consumer<TaskManager> task){
        runTask(new GenericTask(this, name, task));
    }

    public boolean hasTasksUncompleted(){
        return !remainingTasks.isEmpty();
    }

    public List<Task> getTasksUncompleted(){
        return remainingTasks;
    }

    public void taskFinished(Task task) {
        remainingTasks.remove(task);

        for(Consumer<Task> callback : taskCompletionCallbacks)
            callback.accept(task);
    }

     /**
     * Register an event handler for a task completion event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a task is completed
     * @return True if the callback was registered successfully
     */
    public boolean registerTaskCompletionCallback(Consumer<Task> callback){

        // Prevent a callback being triggered twice
        if(taskCompletionCallbacks.contains(callback))
            return false;

        // Store
        taskCompletionCallbacks.add(callback);
        return true;
    }
}