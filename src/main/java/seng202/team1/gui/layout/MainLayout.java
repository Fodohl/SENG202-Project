package seng202.team1.gui.layout;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.util.Duration;
import seng202.team1.api.photon.Feature;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UINavButton;
import seng202.team1.gui.pages.Pages;

import static seng202.team1.common.StringHelpers.featureToAddress;
import static seng202.team1.gui.GraphicsCommon.precentWindowX;

/**
 * Composite UI component that contains elements consistent across the whole app
 *
 * Note: there should be only one per window
 *
 * @todo move search bar to own compontent
 * @credit https://stackoverflow.com/questions/34784037/javafx-8-how-to-add-a-timedelay-to-a-listener deboucer
 */
public class MainLayout extends LayoutElement {

    /// The element that contains the content of the current page
    private ScrollPane content = new ScrollPane();

    /// The element that contains the {@link #content} container
    private BorderPane page = null;

    private Popup searchPopup = new Popup();
    private VBox suggestionContainer = new VBox();
    private TextField searchInput;
    private PauseTransition debouncer;

    private HBox searchBar;

    /**
     * Construct a new CoreUI for a given window
     *
     * @param windowManager The reference to the window this UI & its pages will be shown on
     */
    public MainLayout(WindowManager windowManager) {
        super(windowManager);

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
                navbar.getChildren().add(new UINavButton(windowManager, option));

        // Settings at the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        HBox navSettings = new UINavButton(windowManager, Pages.SETTINGS);

        // Combine
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(precentWindowX(0.15));
        sidebar.getChildren().addAll(logo, navbar, spacer, navSettings);
        return sidebar;

    }

    private void setupPopup() {

        // Style
        suggestionContainer.getStyleClass().add("search-suggestions-popup");
        suggestionContainer.setMaxHeight(200);

        // Scroll if there are lots of results
        ScrollPane suggestionScroll = new ScrollPane(suggestionContainer);
        suggestionScroll.setFitToWidth(true);
        suggestionScroll.setMaxHeight(200);
        suggestionScroll.getStyleClass().add("search-suggestions-scroll");

        // Close if user clicks outside popup
        searchPopup.getContent().add(suggestionScroll);
        searchPopup.setAutoHide(true);
    }

    private void search(String address){

        // Prevent API error when user clears their input
        if(address.isEmpty())
            return;

        windowManager.getGlobalState().getPhotonClient().searchLocation(address, locationResponse -> {

            // Clear previous search addresses
            suggestionContainer.getChildren().clear();

            // Dont show an empty pop up
            if (locationResponse.features().isEmpty()) {
                searchPopup.hide();
                return;
            }

            // Add all the found locations
            for(Feature location : locationResponse.features())
                suggestionContainer.getChildren().add(makeLocationLable(location));

            // Force the popup to be below the search bar
            Bounds bounds = searchBar.localToScreen(searchBar.getBoundsInLocal());
            searchPopup.show(searchBar.getScene().getWindow(), bounds.getMinX() + 20, bounds.getMaxY());
        });
    }

    private Label makeLocationLable(Feature location) {

        String addressText = StringHelpers.featureToAddress(location);
        Label suggestion = new Label(addressText);

        suggestion.setOnMouseClicked(e -> {
            searchInput.setText(addressText);
            submitSearch(location);
        });

        return suggestion;
    }

    private void submitSearch(Feature location) {

        windowManager.getGlobalState().setMapSearchRequest(location);

        // Hide the popup and cancel any address resolutions
        debouncer.pause();
        searchPopup.hide();
    }


    private void handleSearchSubmitAction(ActionEvent actionEvent) {


        // Submit the first location found (known to have the highest likelyhood)
         windowManager.getGlobalState().getPhotonClient().searchLocation(searchInput.getText(), locationResponse -> {

             //@todo err: not a known place
             if(locationResponse.features().isEmpty())
                 return;

             submitSearch(locationResponse.features().getFirst());
        });

    }

    /**
     * Build a header containing the app search bar and account info
     *
     * @return The header for the app
     */
    private Parent header() {

        // Search images
        UIImage searchIcon = new UIImage("/media/images/search.svg", 16, 16);

        // Search text
        searchInput = new TextField();
        searchInput.setPromptText("Enter address...");
        searchInput.getStyleClass().add("search-input");

        // Submit action
        searchInput.setOnAction(this::handleSearchSubmitAction);

        // Ensure API is not overwhelmed
        debouncer = new PauseTransition(Duration.millis(300));
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
                debouncer.setOnFinished(event -> search(newValue));
                debouncer.playFromStart();
            }
        );

        // Build Search Bar
        searchBar = new HBox(searchIcon, searchInput);
        searchBar.getStyleClass().add("search-bar");

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

        setupPopup();

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
