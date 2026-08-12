package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.data.Bin;
import seng202.team1.gui.components.UIMap;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

/**
 * Page that displays a map with bin locations
 */
public class MapPage extends Page {

    /// The map the page uses
    private UIMap map = null;

    /**
     * Constructs a new MapPage
     *
     * @param windowManager the window this page is on
     */
    public MapPage(WindowManager windowManager) {

        super(windowManager, PageType.DASHBOARD, true);

    }

    /**
     * Constructs the UI for the map page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        VBox sidebar = new VBox();

        // Populate the map
        map = new UIMap();
        for(Bin bin : windowManager.getGlobalState().getDataLoader().getBinObjects())
            map.addPoint(bin.getBinLatitude(), bin.getBinLongitude(), bin.getBinID());

        // Handle map events
        map.registerPointClickCallback(this::onMarkerClick);

        return new StackPane(sidebar, map);
    }

    private void onMarkerClick(Integer binID){
        System.out.println(binID);
    }
}
