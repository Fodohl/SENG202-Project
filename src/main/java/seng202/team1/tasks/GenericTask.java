package seng202.team1.tasks;

import seng202.team1.state.GlobalState;

import java.util.function.Consumer;

/**
 * A wrapper to turn any function into a task
 */
public class GenericTask extends Task{

    /// The task function to run
    private Consumer<TaskManager> task;

    /**
     * Constructs a new task
     *
     * @param taskManager The task manage responsible for this task
     * @param name The name of what the task does
     * @param task The task function to run
     */
    public GenericTask(TaskManager taskManager, String name, Consumer<TaskManager> task) {
        super(taskManager, name);

        this.task = task;
    }

    /**
     * Run the provided task
     *
     * @param manager The task manager that triggered the task
     */
    @Override
    public void task(GlobalState state) {
        task.accept(manager);
    }
}
