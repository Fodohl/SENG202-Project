package seng202.team1.gui.screens;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.data.Bin;
import seng202.team1.gui.components.UIMap;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.ScreenManager;

public class MapScreen extends LayoutElement {

    private UIMap map = null;

    public MapScreen(ScreenManager screenManager) {

        super(screenManager, ScreenType.DASHBOARD);

    }

    @Override
    protected Parent render_self() {

        VBox sidebar = new VBox();

        // Populate the map
        map = new UIMap();
        for(Bin bin : screenManager.getGlobalState().getDataLoader().getBinObjects())
            map.addPoint(bin.getBinLatitude(), bin.getBinLongitude());

        return new StackPane(sidebar, map);
    }
}
