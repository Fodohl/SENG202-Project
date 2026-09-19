package seng202.team1.gui.layout;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UILocationInput;
import seng202.team1.gui.components.UINavButton;
import seng202.team1.gui.pages.Pages;

import static seng202.team1.gui.GraphicsCommon.precentWindowX;

/**
 * Composite UI component that contains elements consistent across the whole app
 *
 * Note: there should be only one per window
 */
public class MainLayout extends LayoutElement {

    /// The element that contains the content of the current page
    private ScrollPane content = new ScrollPane();

    /// The element that contains the {@link #content} container
    private BorderPane page = null;

    /**
     * Construct a new CoreUI for a given window
     *
     * @param router The reference to the window this UI & its pages will be shown on
     */
    public MainLayout(Router router) {
        super(router);

    }

    /**
     * Build a sidebar containing the app logo and navigation buttons
     *
     * @return The sidebar for the app
     */
    private Parent sidebar() {

        // Logo
        HBox logo = new HBox(10);
        logo.getStyleClass().add("logo");
        StackPane logoIcon = new UIImage("/media/images/logo.svg", 44, 44);

        // Title
        Label logoTitle = new Label("TrashApp");
        logoTitle.getStyleClass().add("logo-h1");
        logo.getChildren().addAll(logoIcon, logoTitle);

        // Navigation Menu
        VBox navbar = new VBox(8);
        for (Pages option : Pages.values())
            if(option != Pages.SETTINGS)
                navbar.getChildren().add(new UINavButton(router, option));

        // Settings at the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        HBox navSettings = new UINavButton(router, Pages.SETTINGS);

        // Combine
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(precentWindowX(0.15));
        sidebar.getChildren().addAll(logo, navbar, spacer, navSettings);
        return sidebar;

    }


    /**
     * Build a header containing the app search bar and account info
     *
     * @return The header for the app
     */
    private Parent header() {

        // Search Bard
        UILocationInput searchBar = new UILocationInput(getRouter().getGlobalState());

        // Account
        Label accountLabel = new Label("Account (DB)");
        accountLabel.getStyleClass().add("account-text");
        BorderPane.setAlignment(accountLabel, Pos.CENTER_RIGHT);

        // Combine
        BorderPane header = new BorderPane();
        header.getStyleClass().add("header");
        header.setPrefHeight(60);
        header.setRight(accountLabel);
        header.setLeft(searchBar);

        return header;
    }

    /**
     * Combine UI elements to build the CoreUI
     *
     * @return A new root node containing the rendered CoreUI
     */
    @Override
    protected Parent renderSelf() {

        // Page
        page = new BorderPane();
        page.getStyleClass().add("page");
        page.setTop(header());

        // Setup page content
        page.setCenter(content);
        content.getStyleClass().add("content");
        content.setFitToWidth(true);
        content.setFitToHeight(true);

        // Root
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        root.setLeft(sidebar());
        root.setCenter(page);
        return root;
    }

    /**
     * Update the contents section to show a new pages
     *
     * @param rendered The rendered page to show
     */
    public void swap(Parent rendered) {

        // Change the content to the new page
        content.setContent(rendered);
    }

    /**
     * Get the dynamic page content of the app
     *
     * @return The page component inside the ui
     */
    public BorderPane getPage(){
        return page;
    }
}
