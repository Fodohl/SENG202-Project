package seng202.team1.gui.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.WasteType;
import seng202.team1.gui.components.*;
import seng202.team1.gui.WindowManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Page that displays a map with bin locations
 *
 * @todo: when waste is merged in change dropdown values
 */
public class MapPage extends Page {

    /// The shown bin, null if no bin is being shown right now
    private Integer selectedBin = null;

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

    private UIDropDown typeInput;
    private UITextInput weightInput;
    private UISlider statusInput;
    private UIParagraphInput reportInput;

    private Double userPosLat = 0.0;
    private Double userPosLong = 0.0;
    private int userPosMarkerID = 0xFFFFFFFF;

    /**
     * Constructs a new MapPage
     *
     * @param windowManager the window this page is on
     */
    public MapPage(WindowManager windowManager) {

        super(windowManager, PageType.DASHBOARD, true);

    }

    public VBox binImage(){

        // Load the image
        UIImage bin = new UIImage("/images/bin.png", 70, 130);
        bin.getStyleClass().add("img");

        // Style the container
        VBox container = new VBox(bin);
        container.getStyleClass().add("bin-icon");
        return container;
    }

    private UIModal entryModal(){

        // Prepare conents
        List<String> trashTypes = Arrays.stream(WasteType.values()).map(t -> StringHelpers.enumNameToTitle(t.name())).toList();

        // Log data
        UIIconText typeLabel = new UIIconText("bin", "Trash Type");
        typeInput = new UIDropDown("Select waste type...", trashTypes);
        UIIconText weightLabel = new UIIconText("scales", "Estimated Weight");
        weightInput = new UITextInput("Enter waste weight...");
        UISection logInputSection = new UISection(15, typeLabel, typeInput, weightLabel, weightInput);

        // Bin data
        UIIconText statusLabel = new UIIconText("bin-full", "Estimated Fullness");
        statusInput = new UISlider();
        UISection binInputSection = new UISection(15, statusLabel, statusInput);

        // Actions
        UIButton submit = new UIButton("Submit Log");
        UIButton cancel = new UIButton("Cancel", true);
        UISection actionsSection = new UISection(15, submit, cancel);

        // Action handlers
        submit.setOnMouseClicked(e -> submitTrashLog());
        cancel.setOnMouseClicked(e -> closeModal());

        // Center buttons
        actionsSection.getStyleClass().add("buttons");
        actionsSection.setAlignment(Pos.CENTER);

        VBox contents = new VBox(logInputSection, binInputSection, actionsSection);
        return new UIModal("/images/log.svg", "Log Trash", contents);
    }

    private UIModal reportModal(){

        // Bin data
        UIIconText reportLabel = new UIIconText("log", "Report Info");
        reportInput = new UIParagraphInput("Enter the issue with this bin...");
        UIIconText statusLabel = new UIIconText("bin-full", "Estimated Fullness");
        statusInput = new UISlider();
        UISection binInputSection = new UISection(15, reportLabel, reportInput, statusLabel, statusInput);

        // Actions
        UIButton submit = new UIButton("Submit Report");
        UIButton cancel = new UIButton("Cancel", true);
        UISection actionsSection = new UISection(15, submit, cancel);

        // Action handlers
        submit.setOnMouseClicked(e -> submitReport());
        cancel.setOnMouseClicked(e -> closeModal());

        // Center buttons
        actionsSection.getStyleClass().add("buttons");
        actionsSection.setAlignment(Pos.CENTER);

        VBox contents = new VBox(binInputSection, actionsSection);
        return new UIModal("/images/warning.svg", "Report Issue", contents);

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
        UIModal logEntryModal = entryModal();
        UIModal reportIssue = reportModal();
        directionsButton.setOnMouseClicked(e -> waitForLocation());
        logButton.setOnMouseClicked(e -> windowManager.showOverlay(logEntryModal));
        issueButton.setOnMouseClicked(e -> windowManager.showOverlay(reportIssue));

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
            map.addPoint(bin.getLatitude(), bin.getLongitude(), bin.getID(), bin.getType().toString().toLowerCase());

        // Handle map events
        map.registerPointClickCallback(this::onMarkerClick);
        map.registerPossClickCallback(this::onPosClick);

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
    }

    private void onMarkerClick(Integer binID) {

        // Toggle the bin
        if (Objects.equals(selectedBin, binID))
            selectedBin = null;
        else
            selectedBin = binID;

        clearPreviousBin();
        updateContents();
    }

    private void onPosClick(Double lat, Double lng) {

        // Bin un-selected
        if(!isWaitingForLocation){
            selectedBin = null;
            updateContents();
            removeDirections();
            return;
        }

        userPosLat = lat;
        userPosLong = lng;
        Bin bin = windowManager.getGlobalState().getDataLoader().getBinById(selectedBin);


        // Display user location on map
        removeDirections();
        map.addPoint(userPosLat, userPosLong, userPosMarkerID, "default");
        map.setRoute(userPosLat, userPosLong, bin.getLatitude(), bin.getLongitude());

        stopWaitingForLocation();
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
        if (hidden) return;

        // Get the bin
        Bin bin = windowManager.getGlobalState().getDataLoader().getBinById(selectedBin);

        sidebarBinID.setText(bin.getID().toString());
        sidebarBinType.setText(StringHelpers.enumNameToTitle(bin.getType().name()));
        sidebarBinStatus.setText(String.format("%s%% Full (estimate)", bin.getStatus().toString()));
        sidebarBinUpdated.setText(StringHelpers.toLocaleString(bin.getUpdated()));
        sidebarBinAddress.setText(bin.getAreaName());
        sidebarBinTimesUsed.setText("1");
        sidebarBinLastUsed.setText("N/A");

        // Reset the input
        statusInput.setInput(bin.getStatus());
        clearInput();

    }

    private void submitTrashLog(){

        // Save

        // Errors
        typeInput.setError("Invalid type");
        weightInput.setError("NAN");
        statusInput.setError("To much");


//        closeTrashLog();
    }

    private void submitReport(){

        // Save

        // Errors
        reportInput.setError("Not enough chars");
        statusInput.setError("To much");


//        closeTrashLog();
    }

    private void closeModal(){

        clearInput();
        windowManager.hideOverlay();

    }

    private void clearInput() {

        // Clear previous input
        typeInput.clear();
        weightInput.clear();
        statusInput.clear();
    }

    private void clearPreviousBin(){

        clearInput();
        closeModal();
        removeDirections();

    }

    private void waitForLocation(){

        isWaitingForLocation = true;
        mapDialog.setVisible(true);

    }

    private void stopWaitingForLocation(){
        isWaitingForLocation = false;
        mapDialog.setVisible(false);
    }

    private void removeDirections(){
        map.removePoint(userPosMarkerID);
        map.clearRoute();
    }
}
