package seng202.team1.tasks;

import javafx.application.Platform;

public abstract class Task implements Cloneable{

    private TaskManager manager;

    private String taskName;

    private Long taskID = -1L;

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

   @Override
    public Task clone() {
       try {
           return (Task) super.clone();
       } catch (CloneNotSupportedException e) {
           throw new RuntimeException(e);
       }
   }

    public Task instantiate(Long taskID) {

        Task instance = this.clone();
        instance.taskID = taskID;

        return instance;
    }

    public Long getID() {
        return taskID;
    }
}
