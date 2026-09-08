package seng202.team1.gui.components;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;
import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.layout.LayoutElement;
import seng202.team1.state.GlobalState;

public class UILocationInput extends HBox {


    private Popup searchPopup = new Popup();
    private VBox suggestionContainer = new VBox();
    private TextField searchInput;
    private PauseTransition debouncer;

    private UIKeyValuePair error;
    private UISpinner loader;

    private GlobalState globalState;
    private PhotonClient photonClient;

    public UILocationInput(LayoutElement parent){

        this.globalState = parent.getWindow().getGlobalState();
        this.photonClient = globalState.getPhotonClient();

        setupPopup();

        // Search Images
        UIImage searchIcon = new UIImage("/media/images/search.svg", 16, 16);
        loader = new UISpinner(20,20);
        loader.setVisible(false);

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

        error = new UIKeyValuePair("Error", "none");

        // Build Search Bar
        getChildren().addAll(searchIcon, searchInput, loader);
        getStyleClass().add("search-bar");
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

        photonClient.searchLocation(address, locationResponse -> {

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
            Bounds bounds = this.localToScreen(this.getBoundsInLocal());
            searchPopup.show(this.getScene().getWindow(), bounds.getMinX() + 20, bounds.getMaxY());
        }, error, loader);
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

        globalState.setMapSearchRequest(location);

        // Hide the popup and cancel any address resolutions
        debouncer.pause();
        searchPopup.hide();
    }


    private void handleSearchSubmitAction(ActionEvent actionEvent) {


        // Submit the first location found (known to have the highest likelyhood)
        photonClient.searchLocation(searchInput.getText(), locationResponse -> {

             //@todo err: not a known place
             if(locationResponse.features().isEmpty())
                 return;

             submitSearch(locationResponse.features().getFirst());
        });

    }

}
