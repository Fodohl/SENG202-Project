package seng202.team1.gui.pages;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.data.route.RouteInstruction;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UIKeyValuePair;
import seng202.team1.gui.components.UIMap;
import seng202.team1.gui.components.UISection;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.modals.Modals;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.photon.Feature;
import seng202.team1.types.trashlog.TrashLog;

/**
 * Page that displays a map with bin locations
 */
public class MapPage extends Page {

    /// The shown bin, null if no bin is being shown right now
    private Bin selectedBin = null;

    /// Whether the map is waiting for the user to click to set their location
    private boolean isWaitingForLocation = false;

    /// The map the page uses
    private UIMap map;

    /// The sidebar that contains bin details
    private VBox renderedSidebarBinInfo;

    /// The sidebar that contains bin details
    private VBox renderedSidebarUnselected;

    /// Butto to only show if user has a location, lets them clear it
    private UIButton clearlocation;

    /// The dialog box prompting the user to perform an action
    private HBox mapDialog;

    /// The sidebar field for the bin's id
    private UIKeyValuePair sidebarBinID;

    /// The sidebar field for the bin's image
    private UIImage binImage;

    /// The sidebar field for the bin's type
    private UIKeyValuePair sidebarBinType;

    /// The sidebar field for the bin's fullness
    private UIKeyValuePair sidebarBinStatus;

    /// The sidebar field for the bin's last upadte time
    private UIKeyValuePair sidebarBinUpdated;

    /// The sidebar field for the bin's location
    private UIKeyValuePair sidebarBinAddress;

    /// The sidebar field for the bin's usage count for this user
    private UIKeyValuePair sidebarBinTimesUsed;

    /// The sidebar field for the bin's last use time for this user
    private UIKeyValuePair sidebarBinLastUsed;

    /// Latitude location of the user
    private Double userPosLat = null;

    /// Longitude location of the user
    private Double userPosLong = null;

    /// ID for the marker of the users location
    private int userPosMarkerID = 0xFFFFFFFF;

    /// The id of the modal that is currently showing the directions to a bin, -1 if none opened yet
    private long directionsModalID = -1;

    /// The element containing the page
    private StackPane root;

    /**
     * Constructs a new MapPage
     *
     * @param router the window this page is on
     */
    public MapPage(Router router) {

        super(router, Pages.MAP);

    }

    /**
     * Build the bin image part of the sidebar
     *
     * @return The bin image part of the sidebar
     */
    public VBox binImage(){

        // Load the image
        binImage = new UIImage("/media/images/bin.png", 340, 160);
        binImage.getStyleClass().add("img");

        // Style the container
        VBox container = new VBox(binImage);
        container.getStyleClass().add("bin-icon");
        return container;
    }

    /**
     * Build the bin info sidebar
     *
     * @return The bin info sidebar
     */
    private VBox sidebarBinInfo() {

        // Title
        UITitle title = new UITitle("Bin Details");
        UISection titleSection = new UISection(title);

        // Bin info
        sidebarBinID = new UIKeyValuePair("ID", "");
        VBox sidebarBinImageContainer = binImage();
        sidebarBinType = new UIKeyValuePair("Type", "");
        sidebarBinStatus = new UIKeyValuePair("Status", "");
        sidebarBinUpdated = new UIKeyValuePair("Updated", "");
        UISection infoSection = new UISection(sidebarBinID, sidebarBinImageContainer, sidebarBinType, sidebarBinStatus, sidebarBinUpdated);

        // Address
        sidebarBinAddress = new UIKeyValuePair("Address", "");
        UISection addressSection = new UISection(sidebarBinAddress);

        // Usage
        sidebarBinTimesUsed = new UIKeyValuePair("Times Used", "");
        sidebarBinLastUsed = new UIKeyValuePair("Last Used", "");
        UISection collectionSection = new UISection(sidebarBinTimesUsed, sidebarBinLastUsed);

        // Actions
        UIButton directionsButton = new UIButton("Get Directions");
        UIButton logButton = new UIButton("Log Entry");
        UIButton issueButton = new UIButton("Report Issue");
        UISection actionsSection = new UISection(15, directionsButton, logButton, issueButton);
        actionsSection.getStyleClass().add("buttons");

        // Modals
        directionsButton.setOnMouseClicked(e -> tryDrawDirections());
        logButton.setOnMouseClicked(e -> router.openModal(Modals.LOG_TRASH, selectedBin));
        issueButton.setOnMouseClicked(e -> router.openModal(Modals.REPORT_ISSUE, selectedBin));

        // Combine
        VBox box = new VBox(titleSection, infoSection, addressSection, collectionSection, actionsSection);
        box.getStyleClass().add("pop-over-side-bar");
        StackPane.setAlignment(box, Pos.CENTER_RIGHT);
        return box;
    }

    /**
     * Sidebar to show when no bin is selected
     *
     * @return The sidebar element
     */
    private VBox sideBarUnselected(){


        Label message = new Label("Please click a bin on the map to see its info here or log an item of trash");

        clearlocation = new UIButton("Click Here To Clear Your Selected Location");
        clearlocation.setOnMouseClicked(e -> clearUserLocation());
        clearlocation.setVisible(false);

        // Combine
        VBox box = new VBox(30, message, clearlocation);
        box.getStyleClass().addAll("pop-over-side-bar", "pop-over-side-bar-unselected");
        StackPane.setAlignment(box, Pos.CENTER_RIGHT);
        return box;
    }

    /**
     * Constructs the UI for the map page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        // Setup the sidebar
        renderedSidebarBinInfo = sidebarBinInfo();
        renderedSidebarUnselected = sideBarUnselected();

        // Populate the map
        map = new UIMap();
        for (Bin bin : getState().getBinDAO().getAll())
            map.addPoint(bin.getLatitude(), bin.getLongitude(), bin.getID(), bin.getCategory().toString().toLowerCase());

        // Handle map events
        map.registerPointClickCallback(this::onMarkerClick);
        map.registerPossClickCallback(this::onPosClick);
        map.registerMapRouted(this::onMapRouted);

        // Refresh sidebar on log
        router.registerModalChangeCallback(e -> {
            if(e == Modals._CLOSED)
                refreshSidebar();
        });

        // Map messages
        Label mapDialogText = new Label("Click your location on the map to get directions");
        mapDialog = new HBox(mapDialogText);

        // Style such that it is top center
        mapDialog.getStyleClass().add("map-dialog");
        StackPane.setAlignment(mapDialog, Pos.TOP_CENTER);
        StackPane.setMargin(mapDialog, new Insets(0, 250, 0, 0));
        mapDialogText.setAlignment(Pos.CENTER);
        HBox.setHgrow(mapDialogText, Priority.ALWAYS);

        root = new StackPane(map, mapDialog, renderedSidebarUnselected);
        return root;
    }

    /**
     * Refresh the sidebar
     */
    @Override
    public void updateContents() {

        stopWaitingForLocation();
        refreshSidebar();
        handleLocationRequest();
        handleShowRequest();
    }

    private void handleShowRequest() {

        // No requested location
        Integer binID = getState().consumeBinShowRequest();
        if(binID == null)
            return;

        map.showBin(binID);
    }

    /**
     * Convert a location search event into user map position
     */
    private void handleLocationRequest() {

        // No requested location
        Feature location = getState().consumeMapSearchRequest();
        if(location == null)
            return;

        // Invalid location
        List<Double> coords = location.geometry().coordinates();
        if(coords.size() != 2)
            return;

        removeDirections();
        setUserLocation(coords.getLast(), coords.getFirst());
    }

    /**
     * Record the location of the user
     *
     * @param lat The latitude position of the user
     * @param lng The longitude position of the user
     */
    private void setUserLocation(Double lat, Double lng) {

        userPosLat = lat;
        userPosLong = lng;
        clearlocation.setVisible(true);

        // Hide outdated user position
        map.removePoint(userPosMarkerID);

        // Center on user's new position
        map.addPoint(userPosLat, userPosLong, userPosMarkerID, "default");
        map.centreOnPoint(userPosMarkerID);

    }

    /**
     * Show a bin in the sidebar when it is clicked on the map
     *
     * @param binID The ID of the bin that was clicked
     */
    private void onMarkerClick(Integer binID) {

        // Clicking on the user marker does nothing
        if(binID == -1)
            return;

        // Toggle the bin
        if (Objects.equals(selectedBin, binID))
            selectedBin = null;
        else
            selectedBin = getState().getBinDAO().getOne(binID);

        clearPreviousBin();
        updateContents();
    }

    /**
     * Handle when map clicked but not on a marked. If waiting for location interprets as the users location, otherwise
     * interprets as clicking off a previously clicked bin
     *
     * @param lat The click position in the world latitudinally
     * @param lng The click position in the world longitudinally
     */
    private void onPosClick(Double lat, Double lng) {

        // Bin un-selected
        if(!isWaitingForLocation){
            selectedBin = null;
            updateContents();
            removeDirections();
            return;
        }

        // Clear old state
        setUserLocation(lat, lng);
        stopWaitingForLocation();

        // Can now draw the directions once the start point is known
        tryDrawDirections();
    }

    /**
     * Show a popup of directions to a given bin
     *
     * @param instructions The direction steps to the bin
     */
    private void onMapRouted(List<RouteInstruction> instructions){
        directionsModalID = router.spawnModal(Modals.DIRECTIONS, instructions);
    }

    /**
     * Update the contents of the status bar to show the selected bin (if any). If no bin is selected sidebar is not shown
     */
    private void refreshSidebar() {

        // Sidebar hidden
        boolean hidden = selectedBin == null;
        root.getChildren().set(2, hidden ? renderedSidebarUnselected : renderedSidebarBinInfo);

        if (hidden)
            return;

        // Get usage
        List<TrashLog> uses = getState().getTrashLogDAO()
                .getAll().stream()
                .filter(l -> l.binID() == selectedBin.getID())
                .sorted(Comparator.comparing(TrashLog::createdAt))
                .toList();
        TrashLog newest = uses.isEmpty() ? null : uses.getLast();

        // Refresh the bins' average status
        selectedBin = getRouter().getGlobalState().getBinDAO().getOne(selectedBin.getID());

        sidebarBinID.setText(selectedBin.getID().toString());
        binImage.setSource(selectedBin.getPhotoURL() != null ? selectedBin.getPhotoURL() : "/media/images/bin.png");
        sidebarBinType.setText(StringHelpers.enumNameToTitle(selectedBin.getCategory().name()));
        sidebarBinStatus.setText(String.format("%s (estimate)", StringHelpers.enumNameToTitle(selectedBin.getFullness().name())));
        sidebarBinUpdated.setText(StringHelpers.toLocaleString(selectedBin.getUpdated()));
        sidebarBinAddress.setText(selectedBin.getAreaName());
        sidebarBinTimesUsed.setText(String.format("%d", uses.size()));
        sidebarBinLastUsed.setText(newest == null ? "N/A " : StringHelpers.toLocaleString(newest.disposedAt()));
    }

    /**
     * Clear the UI state of the last shown bin
     */
    private void clearPreviousBin(){

        router.hideOverlay();
        removeDirections();

    }

    /**
     * Prompt the user to click a location on the map and prepare to interpret that click
     *
     * @return True if have to wait for the location, false if already have it
     */
    private boolean waitForLocation(){

        // Already have location
        if(userPosLat != null && userPosLong != null)
            return false;

        isWaitingForLocation = true;
        mapDialog.setVisible(true);

        return true;
    }

    /**
     * Stop waiting for the user to click a place on the map as their location
     */
    private void stopWaitingForLocation(){
        isWaitingForLocation = false;
        mapDialog.setVisible(false);
    }

    /**
     * Attempt to render the route to a bin
     */
    private void tryDrawDirections(){

        // Dont have a dest point
        if(selectedBin == null)
            return;

        // Dont have a source point (get it)
        if(waitForLocation())
            return;

        map.setRoute(userPosLat, userPosLong, selectedBin.getLatitude(), selectedBin.getLongitude());
    }

    /**
     * Clear directions from the map and close the instructions popup
     */
    private void removeDirections(){
        router.killModal(directionsModalID);
        map.clearRoute();
    }

    /**
     * Remove the user's location
     */
    private void clearUserLocation() {
        userPosLong = null;
        userPosLat = null;
        clearlocation.setVisible(false);

        map.removePoint(userPosMarkerID);
    }
}