package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.Bin;
import seng202.team1.gui.components.*;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

import java.util.Objects;

/**
 * Page that displays a map with bin locations
 */
public class MapPage extends Page {

    /// The shown bin, null if no bin is being shown right now
    private Integer selectedBin = null;

    /// The map the page uses
    private UIMap map;

    private VBox sidebar;

    private UIKeyValuePair sidebarBinID;
    private UIKeyValuePair sidebarBinType;
    private UIKeyValuePair sidebarBinStatus;
    private UIKeyValuePair sidebarBinUpdated;
    private UIKeyValuePair sidebarBinAddress;
    private UIKeyValuePair sidebarBinCollection;

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

    public VBox sidebar() {

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

        // Collection
        sidebarBinCollection = new UIKeyValuePair("Next Collection", "");
        UISection collectionSection = new UISection(sidebarBinCollection);

        // Actions
        // b1
        // b2
        // b3
        UISection actionsSection = new UISection();

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
            map.addPoint(bin.getBinLatitude(), bin.getBinLongitude(), bin.getBinID());

        // Handle map events
        map.registerPointClickCallback(this::onMarkerClick);

        return new StackPane(map, sidebar);
    }

    @Override
    protected void updateContents() {

        refreshSidebar();
    }

    private void onMarkerClick(Integer binID) {

        // Toggle the bin
        if (Objects.equals(selectedBin, binID)) selectedBin = null;
        else selectedBin = binID;

        updateContents();
    }

    private void refreshSidebar() {

        // Sidebar hidden
        boolean hidden = selectedBin == null;
        sidebar.setVisible(!hidden);
        if (hidden) return;

        // Get the bin
        Bin bin = windowManager.getGlobalState().getDataLoader().getBinObjects()
                               .stream().filter(b -> Objects.equals(b.getBinID(), selectedBin))
                               .findFirst().orElseThrow();

        sidebarBinID.setText(bin.getBinID().toString());
        sidebarBinType.setText(StringHelpers.enumNameToTitle(bin.getBinType().name()));

    }
}
