package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UISpinner;
import seng202.team1.gui.phases.Phases;

/**
 * Page that displays a loader while app is getting ready
 */
public class StartingPage extends Page {

    /**
     * Constructs a new StartingPage
     *
     * @param router the window this page is on
     */
    public StartingPage(Router router) {

        super(router, Pages.STARTING);

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

        Label loadingText = new Label("Waiting for 'name o stage' to complete..");

        VBox root = new VBox(50, logo, loadingText);
        root.getStyleClass().add("starting");
        return root;
    }

}
