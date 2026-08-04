package seng202.team1.gui.screens;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team1.data.Bin;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.ScreenManager;
import javafx.scene.control.Label;

import java.util.List;


public class DataScreen extends LayoutElement {

    public DataScreen(ScreenManager screenManager){

        super(screenManager, ScreenType.DASHBOARD);

    }

    @Override
    protected Parent render_self() {

        // Title
        UITitle pageTitle = new UITitle("Raw Data");

        // Data
        List<String> order = List.of("ID", "Latitude", "Longitude", "Type");
        UITable table = new UITable(screenManager.getGlobalState().getDataLoader().toStringTable(), order, false);

        // Combine
        VBox root = new VBox(20, pageTitle, table);
        root.setAlignment(Pos.CENTER);
        return root;
    }
}
