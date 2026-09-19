package seng202.team1.state.preflight;

import javafx.application.Platform;
import seng202.team1.gui.pages.Pages;

import java.util.function.Consumer;

public abstract class PreflightTask {

    private PreflightManager manager;

    private String taskName;

    public PreflightTask(PreflightManager manager, String taskName) {

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

    public abstract void task(PreflightManager manager);
}
