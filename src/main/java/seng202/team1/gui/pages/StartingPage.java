package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UISpinner;
import seng202.team1.gui.phases.Phases;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.preflight.PreflightManager;

/**
 * Page that displays a loader while app is getting ready
 */
public class StartingPage extends Page {

    private Label loadingText;

    private PreflightManager manager;

    /**
     * Constructs a new StartingPage
     *
     * @param router the window this page is on
     */
    public StartingPage(Router router) {

        super(router, Pages.STARTING);

        manager = getState().getPreflightManager();
        manager.registerTaskCompletionCallback(e -> onTaskCompleted(e));


    }

    private void onTaskCompleted(Task e) {

        // Preflight startup done
        if(!manager.hasTasksUncompleted()){
            router.switchPhase(Phases.MAIN);
            return;
        }

        // Show the next task
        updateContents();
    }

    /**
     * Constructs the UI for the starting page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {


        // Logo
        VBox logo = new VBox(10);
        logo.getStyleClass().add("logo");
        UIImage logoIcon = new UIImage("/media/images/logo.svg", 100, 100);

        // Title
        Label logoTitle = new Label("TrashApp");
        logoTitle.getStyleClass().add("logo-h1");

        // Spinner
        UISpinner spinner = new UISpinner(50);
        spinner.getStyleClass().add("load");
        logo.getChildren().addAll(logoIcon, logoTitle, spinner);

        loadingText = new Label("Waiting for 'name o stage' to complete..");

        VBox root = new VBox(100, logo, loadingText);
        root.getStyleClass().add("starting");
        return root;
    }

    @Override
    public void updateContents() {

        loadingText.setText(String.format("Waiting for '%s' to complete..", manager.hasTasksUncompleted() ?
                                                                                   manager.getTasksUncompleted().getFirst().getTaskName()
                                                                                 : "N/A"
                        ));

    }
}
