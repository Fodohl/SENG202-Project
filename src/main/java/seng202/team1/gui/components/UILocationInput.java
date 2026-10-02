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
import seng202.team1.types.photon.Feature;
import seng202.team1.api.photon.PhotonClient;
import seng202.team1.state.GlobalState;

/**
 * A UIComponent to display an autocompleting address input field
 *
 * @credit Debouncer technique re https://stackoverflow.com/questions/34784037/javafx-8-how-to-add-a-timedelay-to-a-listener deboucer
 */
public class UILocationInput extends HBox {

    /// The popup to show to suggest possible addresses
    private Popup searchPopup = new Popup();

    /// The container which holds each suggestion
    private VBox suggestionContainer = new VBox();

    /// The input field where the user input their address
    private TextField searchInput;

    /// Timer to prevent hammering the API whilst typing
    private PauseTransition debouncer;

    /// The text to show any search request errors on
    private UIKeyValuePair error;

    /// The loader to indicate that the address is being searched for
    private UISpinner loader;

    /// A reference to the global application state
    private GlobalState globalState;

    /// A reference to the client connection with the geocoding service
    private PhotonClient photonClient;

    /**
     * Constructs a new UILocationInput
     *
     * @param globalState The reference to application sate
     */
    public UILocationInput(GlobalState globalState){

        this.globalState = globalState;
        this.photonClient = globalState.getPhotonClient();

        setupPopup();

        // Search Images
        UIImage searchIcon = new UIImage("/media/images/search.svg", 16, 16);
        loader = new UISpinner(20);
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

    /**
     * Build the popup, setting up the suggestions container
     */
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

    /**
     * Search for an address and being the fetch for suggestions
     *
     * @param address The address to query for
     */
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


    /**
     * Construct a label for a location suggestion
     *
     * @param location The suggestion
     * @return The formatted location node
     */
    private Label makeLocationLable(Feature location) {

        String addressText = PhotonClient.featureToAddress(location);
        Label suggestion = new Label(addressText);

        suggestion.setOnMouseClicked(e -> {
            searchInput.setText(addressText);
            submitSearch(location);
        });

        return suggestion;
    }

    /**
     * Submit the current resolved location as a search and pass off to the global state
     *
     * @param location The found location
     */
    private void submitSearch(Feature location) {

        globalState.setMapSearchRequest(location);

        // Hide the popup and cancel any address resolutions
        debouncer.pause();
        searchPopup.hide();
    }

    /**
     * Handle the user pressing enter to submit the search, will find the best match to whatever is entered
     *
     * @param actionEvent The submission action
     */
    private void handleSearchSubmitAction(ActionEvent actionEvent) {


        // Submit the first location found (known to have the highest likelyhood)
        photonClient.searchLocation(searchInput.getText(), locationResponse -> {

             if(locationResponse.features().isEmpty())
                 return;

             submitSearch(locationResponse.features().getFirst());
        });

    }

}
