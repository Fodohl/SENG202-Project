package seng202.team1.gui.screens;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team1.data.Bin;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.ScreenManager;
import javafx.scene.control.Label;


public class DataScreen extends LayoutElement {

    public DataScreen(ScreenManager screenManager){

        super(screenManager, ScreenType.DASHBOARD);

    }

    @Override
    protected Parent render_self() {

        // Title
        UITitle pageTitle = new UITitle("Raw Data");

        // Data
        VBox data = new VBox(10);

        System.out.println(screenManager.getGlobalState().getDataLoader().toStringTable());

        // Data row for each bin
        for(Bin bin : screenManager.getGlobalState().getDataLoader().getBinObjects())
            data.getChildren().add(renderBinRow(bin));

        VBox root = new VBox(20, pageTitle, data);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private HBox renderBinRow(Bin bin){

        Label id = new Label(String.format("%d", bin.getBinID()));
        Label latitude = new Label(String.format("%f", bin.getBinLatitude()));
        Label longitude = new Label(String.format("%f", bin.getBinLongitude()));
        Label type = new Label(bin.getBinType().name());

        return new HBox(id, latitude, longitude, type);
    }
}
