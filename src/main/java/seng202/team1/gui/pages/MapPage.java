package seng202.team1.gui.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import seng202.team1.api.photon.Feature;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.route.RouteInstruction;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.gui.components.*;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.modals.Modals;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Page that displays a map with bin locations
 *
 * @todo: when waste is merged in change dropdown values
 */
public class MapPage extends Page {

    /// The shown bin, null if no bin is being shown right now
    private Bin selectedBin = null;

    private boolean isWaitingForLocation = false;

    /// The map the page uses
    private UIMap map;

    private VBox sidebar;

    private HBox mapDialog;

    private UIKeyValuePair sidebarBinID;
    private UIKeyValuePair sidebarBinType;
    private UIKeyValuePair sidebarBinStatus;
    private UIKeyValuePair sidebarBinUpdated;
    private UIKeyValuePair sidebarBinAddress;
    private UIKeyValuePair sidebarBinTimesUsed;
    private UIKeyValuePair sidebarBinLastUsed;


    private Double userPosLat = null;
    private Double userPosLong = null;

    private int userPosMarkerID = 0xFFFFFFFF;
    private long directionsModalID = -1;

    /**
     * Constructs a new MapPage
     *
     * @param windowManager the window this page is on
     */
    public MapPage(WindowManager windowManager) {

        super(windowManager, Pages.MAP, true);

    }

    public VBox binImage(){

        // Load the image
        UIImage bin = new UIImage("/media/images/bin.png", 70, 130);
        bin.getStyleClass().add("img");

        // Style the container
        VBox container = new VBox(bin);
        container.getStyleClass().add("bin-icon");
        return container;
    }

    private VBox sidebar() {

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
        logButton.setOnMouseClicked(e -> windowManager.openModal(Modals.LOG_TRASH, selectedBin));
        issueButton.setOnMouseClicked(e -> windowManager.openModal(Modals.LOG_ISSUE, selectedBin));

        // Combine
        VBox box = new VBox(titleSection, infoSection, addressSection, collectionSection, actionsSection);
        box.getStyleClass().add("pop-over-side-bar");
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
        sidebar = sidebar();

        // Populate the map
        map = new UIMap();
        for (Bin bin : windowManager.getGlobalState().getDataLoader().getBinObjects())
            map.addPoint(bin.getLatitude(), bin.getLongitude(), bin.getID(), bin.getCategory().toString().toLowerCase());

        // Handle map events
        map.registerPointClickCallback(this::onMarkerClick);
        map.registerPossClickCallback(this::onPosClick);
        map.registerMapRouted(this::onMapRouted);

        // Refresh sidebar on log
        windowManager.registerModalChangeCallback(e -> {
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

        return new StackPane(map, mapDialog, sidebar);
    }

    @Override
    protected void updateContents() {

        stopWaitingForLocation();
        refreshSidebar();
        handleLocationRequest();
    }

    private void handleLocationRequest() {

        // No requested location
        Feature location = windowManager.getGlobalState().consumeMapSearchRequest();
        if(location == null)
            return;

        // Invalid location
        List<Double> coords = location.geometry().coordinates();
        if(coords.size() != 2)
            return;

        removeDirections();
        setUserLocation(coords.getLast(), coords.getFirst());
    }

    private void setUserLocation(Double lat, Double lng) {

        userPosLat = lat;
        userPosLong = lng;

        // Remove old point
        map.removePoint(userPosMarkerID);

        // Display user location on map
        map.addPoint(userPosLat, userPosLong, userPosMarkerID, "default");
        map.centreOnPoint(userPosMarkerID);

    }

    private void onMarkerClick(Integer binID) {

        // Clicking on the user marker does nothing
        if(binID == -1)
            return;

        // Toggle the bin
        if (Objects.equals(selectedBin, binID))
            selectedBin = null;
        else
            selectedBin = windowManager.getGlobalState().getDataLoader().getBinById(binID);

        clearPreviousBin();
        updateContents();
    }

    /**
     * when map clickec but no point
     *
     * @param lat
     * @param lng
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

    private void onMapRouted(List<RouteInstruction> instructions){
        directionsModalID = windowManager.spawnModal(Modals.DIRECTIONS, instructions);
    }

    /**
     * Update the contents of the status bar to show the selected bin (if any). If no bin is selected sidebar is not shown
     *
     * @todo image
     */
    private void refreshSidebar() {

        // Sidebar hidden
        boolean hidden = selectedBin == null;
        sidebar.setVisible(!hidden);
        if (hidden)
            return;

        // Get usage
        List<TrashLog> uses = windowManager.getGlobalState().getTrashLogStore()
                                           .getAll().stream()
                                           .filter(l -> l.binLogId() == selectedBin.getID())
                                           .sorted(Comparator.comparing(TrashLog::createdAt))
                                           .toList();
        TrashLog newest = uses.isEmpty() ? null : uses.getLast();

        sidebarBinID.setText(selectedBin.getID().toString());
        sidebarBinType.setText(StringHelpers.enumNameToTitle(selectedBin.getCategory().name()));
        sidebarBinStatus.setText(String.format("%s%% Full (estimate)", selectedBin.getStatus().toString()));
        sidebarBinUpdated.setText(StringHelpers.toLocaleString(selectedBin.getUpdated()));
        sidebarBinAddress.setText(selectedBin.getAreaName());
        sidebarBinTimesUsed.setText(String.format("%d", uses.size()));
        sidebarBinLastUsed.setText(newest == null ? "N/A " : StringHelpers.toLocaleString(newest.disposedAt()));
    }

    private void clearPreviousBin(){

        windowManager.hideOverlay();
        removeDirections();

    }

    private boolean waitForLocation(){

        // Already have location
        if(userPosLat != null && userPosLong != null)
            return false;

        isWaitingForLocation = true;
        mapDialog.setVisible(true);

        return true;
    }

    private void stopWaitingForLocation(){
        isWaitingForLocation = false;
        mapDialog.setVisible(false);
    }

     private void tryDrawDirections(){

        // Dont have a dest point
        if(selectedBin == null)
            return;

        // Dont have a source point (get it)
        if(waitForLocation())
            return;

        map.setRoute(userPosLat, userPosLong, selectedBin.getLatitude(), selectedBin.getLongitude());
    }

    private void removeDirections(){
        windowManager.killModal(directionsModalID);
        map.clearRoute();
    }
}