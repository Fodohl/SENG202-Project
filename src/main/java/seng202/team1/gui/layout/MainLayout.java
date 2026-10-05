package seng202.team1.gui.layout;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIAvatar;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UILocationInput;
import seng202.team1.gui.components.UINavButton;
import seng202.team1.gui.pages.Pages;
import seng202.team1.types.user.Session;
import seng202.team1.types.user.User;

import java.util.List;

import static seng202.team1.gui.GraphicsCommon.precentWindowX;

/**
 * Composite UI component that contains elements consistent across the MAIN phase of the app
 */
public class MainLayout extends Layout {

    /// The element that contains the content of the current page
    private ScrollPane content = new ScrollPane();

    /// The element that contains the {@link #content} container
    private BorderPane page = null;

    /// The element that contains the user's avatar
    private UIAvatar accountIcon;

    /// The element that contains the user's name
    private Label accountLabel;

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
        for (Pages option : router.getPagesInPhase())
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

        // Search Bar
        UILocationInput searchBar = new UILocationInput(getRouter().getGlobalState());

        // Account
        accountLabel = new Label("name");
        accountIcon = new UIAvatar(null);
        accountLabel.getStyleClass().add("account-text");

        // Move account to end
        HBox account = new HBox(10, accountLabel, accountIcon);
        account.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(accountLabel, Pos.CENTER_RIGHT);
        account.setOnMouseClicked(e -> router.switchPage(Pages.SETTINGS));

        // Combine
        BorderPane header = new BorderPane();
        header.getStyleClass().add("header");
        header.setPrefHeight(60);
        header.setRight(account);
        header.setLeft(searchBar);

        return header;
    }

    /**
     * Combine UI elements to build the layout
     *
     * @return A new root node containing the rendered layout
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

        // Show the top of the new page
        content.setContent(rendered);
        content.setVvalue(0);
    }

    /**
     * Get the dynamic page content of the app
     *
     * @return The page component inside the ui
     */
    public BorderPane getPage(){
        return page;
    }

    @Override
    public void refreshLayout() {

        // Check if the user is actually logged in
        Session session = getState().getAuth().getSession();
        if(session == null)
            return;

        User user = session.userDetails();
        accountLabel.setText(user.name());
        accountIcon.setUser(user);
    }
}
