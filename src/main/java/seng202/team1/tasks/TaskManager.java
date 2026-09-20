package seng202.team1.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.components.UIImage;
import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;

import java.util.*;
import java.util.function.Consumer;

public class TaskManager extends StateReferrer {

    /// The event handlers triggered when switching pages
    private final List<Consumer<Task>> taskCompletionCallbacks = new ArrayList<>();

    /// TaskManager specific logger
    private static final Logger log = LogManager.getLogger(TaskManager.class);

    private HashMap<Long, Task> remainingTasks = new HashMap<>();

    private HashMap<Task, Set<Long>> waitingTasks = new HashMap<>();

    private Long nextTaskID = 0L;

    public TaskManager(GlobalState state){
        super(state);
    }

    private void startTask(Task task) {

        log.info("Starting task {} : {}", task.getID(), task.getTaskName());

        Thread thread = new Thread(() -> task.run());
        thread.start();
    }

    public long runTask(Task task, Long... deps){

        // Store the task
        Long taskID = nextTaskID++;
        Task taskInstance = task.instantiate(taskID);
        remainingTasks.put(taskID, taskInstance);

         // Add any deps that are waiting to be completed
        Set<Long> remaingDependicies = new HashSet<>(Arrays.asList(deps).stream()
                .filter(dep -> remainingTasks.keySet().contains(dep))
                .toList());

        // Task doesnt need to wait
        if(remaingDependicies.isEmpty())
            startTask(taskInstance);
        else
            waitingTasks.put(taskInstance, remaingDependicies);

        return taskID;
    }

    public void runTask(String name, Consumer<TaskManager> task, Long... deps){
        runTask(new GenericTask(this, name, task), deps);
    }

    public boolean hasTasksUncompleted(){
        return !remainingTasks.isEmpty();
    }

    public List<Task> getTasksUncompleted(){
        return remainingTasks.values().stream().toList();
    }

    public void taskFinished(Task task) {

        remainingTasks.remove(task.getID());
        updateWaitingQueue(task.getID());

        // Let handlers know
        for(Consumer<Task> callback : taskCompletionCallbacks)
            callback.accept(task);
    }

    private void updateWaitingQueue(Long taskID) {

        for(Task task : waitingTasks.keySet()){

            Set<Long> remaingDependicies = waitingTasks.get(task);

             // Stop waiting for the task if applicable
            if(remaingDependicies.contains(taskID))
                remaingDependicies.remove(taskID);

            // Ready to run
            if(remaingDependicies.isEmpty()) {
                startTask(task);
                waitingTasks.remove(task);
            }
        }
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