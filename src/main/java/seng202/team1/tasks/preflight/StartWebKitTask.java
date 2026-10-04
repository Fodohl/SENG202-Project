package seng202.team1.tasks.preflight;

import javafx.application.Platform;
import seng202.team1.gui.components.UIMap;
import seng202.team1.gui.pages.Pages;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;

/**
 * A task to parse the geojson containing the bin dataset
 */
public class StartWebKitTask extends Task {

     /**
     * Create a new ParseDatasetTask
     *
     * @param preflightManager The task manager responsible for this task
     */
    public StartWebKitTask(PreflightManager preflightManager){
        super(preflightManager, "map engine startup");
    }

     /**
     * The job this task carries out
     *
     * @param manager The task manager that triggered the task
     */
    @Override
    public void task(TaskManager manager) {

        // Sleep so that UI can show the correct loading text
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        // Map must be pre-loaded as it causes lag and it also needs points loaded so that data page can highlight them
        Platform.runLater(() -> {
            manager.getGlobalState().getRouter().rerenderPage(Pages.MAP);
        });
    }
}
