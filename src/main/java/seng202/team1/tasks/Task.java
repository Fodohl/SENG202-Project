package seng202.team1.tasks;

import javafx.application.Platform;

public abstract class Task {

    private TaskManager manager;

    private String taskName;

    public Task(TaskManager manager, String taskName) {

        this.manager = manager;
        this.taskName = taskName;
    }

    public void run(){

        task(manager);

        // Pass back to manager on FX thread
        Platform.runLater(() -> {
            manager.taskFinished(this);
        });
    }


    public String getTaskName() {
        return taskName;
    }

    public abstract void task(TaskManager manager);
}
