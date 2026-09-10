package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.WindowManager;

import java.util.Comparator;

/**
 * Page that displays the data used in the app
 */
public class DataPage extends Page {

    /**
     * Constructs a new DataPage
     *
     * @param windowManager the window this page is on
     */
    public DataPage(WindowManager windowManager){

        super(windowManager, Pages.DATA);

    }

    /**
     * Constructs the UI for the data page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        // Title
        UITitle pageTitle = new UITitle("Raw Data");
        pageTitle.setAlignment(Pos.CENTER);

        // Data
       StringTableBuilder<Bin> tableData = new StringTableBuilder<Bin>(windowManager.getGlobalState().getDataLoader().getBinObjects())
            .addColumn("binID", "ID")
            .addColumn("fullness", "Fullness", val -> StringHelpers.enumNameToTitle(((BinFullness)val).name()))
            .addColumn("areaName", "Location")
            .addColumn("type", "Type", val -> StringHelpers.enumNameToTitle(((BinCategory)val).name()))
            .sortBy(Comparator.comparing(Bin::getID));

        UITable table = new UITable(tableData, false);

        // Combine
        VBox root = new VBox(20, pageTitle, table);
        root.setAlignment(Pos.CENTER);
        return root;
    }
}
