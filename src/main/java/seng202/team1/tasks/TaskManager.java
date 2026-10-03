package seng202.team1.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.components.UIImage;
import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Manages state and threading of tasks
 */
public class TaskManager extends StateReferrer {

    /// The event handlers triggered when switching pages
    private final List<Consumer<Task>> taskCompletionCallbacks = new ArrayList<>();

    /// TaskManager specific logger
    private static final Logger log = LogManager.getLogger(TaskManager.class);

    /// A list of tasks, and their ids, that are yet to be completed (including tasks that are waiting to be started)
    private ConcurrentHashMap<Long, Task> remainingTasks = new ConcurrentHashMap<>();

    /// A list of waiting tasks and the tasks they are waiting on
    private ConcurrentHashMap<Task, Set<Long>> waitingTasks = new ConcurrentHashMap<>();

    /// The next available ID that can be assigned to a task
    private static Long nextTaskID = 0L;

    /**
     * Constructs a new TaskManager
     *
     * @param state The global app state
     */
    public TaskManager(GlobalState state){
        super(state);
    }

    /**
     * Run a tasks' job function in a thread
     *
     * @param task The task to thread
     */
    private void startTask(Task task) {

        log.info("Starting task {} : {}", task.getID(), task.getTaskName());

        // During testing, the UI isnt preventing from waiting for tasks to complete in the backgroud, thus race conditions can occur,
        // so muist run in sequental
        if(GlobalState.IS_HEADLESS_MODE){
            task.run();
            return;
        }

        Thread thread = new Thread(() -> task.run());
        thread.start();
    }

    /**
     * Queue a task to be run once its dependencies have finished runnings.
     * Warning: Dependencies must be acyclic
     *
     * @param task The task to queue
     * @param deps The id's of tasks that must be ran before this one can
     * @return The id assigned to this task
     */
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

    /**
     * Run a function as a task
     *
     * @see #runTask(Task, Long...)
     */
    public long runTask(String name, Consumer<TaskManager> task, Long... deps){
        return runTask(new GenericTask(this, name, task), deps);
    }

    /**
     * Get whether this manager is still waiting for tasks to complete
     *
     * @return True if tasks are still running, false otherwise
     */
    public boolean hasTasksUncompleted(){
        return !remainingTasks.isEmpty();
    }

    /**
     * Get a list of tasks that are still executing
     *
     * @return The list of tasks
     */
    public List<Task> getTasksUncompleted(){
        return remainingTasks.values().stream().toList();
    }

    /**
     * Mark a given task as completed
     *
     * @param task The task that was completed
     */
    public void taskFinished(Task task) {

        remainingTasks.remove(task.getID());
        updateWaitingQueue(task.getID());

        // Let handlers know
        for(Consumer<Task> callback : taskCompletionCallbacks)
            callback.accept(task);
    }

    /**
     * Remove a completed task from the queue of dependencies
     *
     * @param taskID The task that was completed
     */
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