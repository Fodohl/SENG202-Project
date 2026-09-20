package seng202.team1.tasks;

import java.util.function.Consumer;

public class GenericTask extends Task{

    private Consumer<TaskManager> task;

    public GenericTask(TaskManager taskManager, String name, Consumer<TaskManager> task) {
        super(taskManager, name);

        this.task = task;
    }

    @Override
    public void task(TaskManager manager) {
        task.accept(manager);
    }
}
