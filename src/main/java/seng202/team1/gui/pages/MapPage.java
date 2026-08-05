package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.data.Bin;
import seng202.team1.gui.components.UIMap;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

public class MapPage extends LayoutElement {

    private UIMap map = null;

    public MapPage(WindowManager windowManager) {

        super(windowManager, PageType.DASHBOARD);

    }

    @Override
    protected Parent render_self() {

        VBox sidebar = new VBox();

        // Populate the map
        map = new UIMap();
        for(Bin bin : windowManager.getGlobalState().getDataLoader().getBinObjects())
            map.addPoint(bin.getBinLatitude(), bin.getBinLongitude());

        return new StackPane(sidebar, map);
    }
}
