package seng202.team1.tasks;

import javafx.application.Platform;

/**
 * An instance of a threadable job
 */
public abstract class Task implements Cloneable{

    /// The task manager that runs this task
    private TaskManager manager;

    /// The name of what the task does (ie waiting for 'x' to complete)
    private String taskName;

    /// The task instance ID or -1 if not instantiated
    private Long taskID = -1L;

    /**
     * Create a new task
     *
     * @param manager The task manage responsible for this task
     * @param taskName The name of what the task does
     */
    public Task(TaskManager manager, String taskName) {

        this.manager = manager;
        this.taskName = taskName;
    }

    /**
     * Run the task and then mark as finished on the main thread (blocks)
     */
    public void run(){

        task(manager);

        // Pass back to manager on FX thread
        Platform.runLater(() -> {
            manager.taskFinished(this);
        });
    }

    /**
     * Get the name of the task to do
     *
     * @return The task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * The task function that the task must preform
     *
     * @param manager The task manager that triggered the task
     * @todo if store manager already then dont need to pass it?
     */
    public abstract void task(TaskManager manager);

    /***
     * Create a clone of a task by copying it
     *
     * @return The task copy
     */
   @Override
    public Task clone() {
       try {
           return (Task) super.clone();
       } catch (CloneNotSupportedException e) {
           throw new RuntimeException(e);
       }
   }

    /**
     * Instantiate a new instance of this task
     *
     * @param taskID The identifier of this task
     * @return The new instance
     */
    public Task instantiate(Long taskID) {

        Task instance = this.clone();
        instance.taskID = taskID;

        return instance;
    }

    /**
     * Get the id of this task
     *
     * @return This task's ID or -1 if it doesn't have one
     */
    public Long getID() {
        return taskID;
    }
}
