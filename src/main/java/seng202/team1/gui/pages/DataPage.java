package seng202.team1.gui.pages;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.data.export.ExportManager;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.components.UIIconButton;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.Router;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Page that displays the data used in the app
 */
public class DataPage extends Page {

    /// Height to hold the page at whilst the table is empty, taller than any screen
    private static final double LOADING_MIN_HEIGHT = 5000;

    /// The container for the whole page
    private VBox root;

    /// The table that contains the app's data
    private UITable table;

    /// The data shown in the table, used for exports
    private StringTableBuilder<Bin> tableData;

    /**
     * Constructs a new DataPage
     *
     * @param router the window this page is on
     */
    public DataPage(Router router){

        super(router, Pages.DATA);

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

        UIIconButton exportButton = new UIIconButton("/media/images/export.svg", "Export Data");
        exportButton.getStyleClass().add("data-button");

        // Download the table to CSV for exports
        exportButton.setOnMouseClicked(e -> {
            ExportManager.exportToCSV(router.getStage(), ExportManager.exportName("bin_data"), tableData.build());
        });

        tableData = new StringTableBuilder<Bin>(new ArrayList<>())
            .addColumn("binID", "ID")
            .addColumn("fullness", "Fullness", val -> StringHelpers.enumNameToTitle(((BinFullness)val).name()))
            .addColumn("areaName", "Location")
            .addColumn("type", "Type", val -> StringHelpers.enumNameToTitle(((BinCategory)val).name()))
            .sortBy(Comparator.comparing(Bin::getID));

        table = new UITable(tableData, false);
        table.getStyleClass().add("data-table");
        table.setOnRowClick(row -> getState().goToBin(row.get("ID")));

        root = new VBox(20, pageTitle, exportButton, table);
        root.setAlignment(Pos.TOP_CENTER);

        fillTable();

        return root;
    }

    /**
     * Show the page with an empty table straight away, then fill in the rows once it is on screen
     */
    @Override
    public void updateContents() {

        tableData.updateData(new ArrayList<>());
        table.buildTableContent();

        root.setMinHeight(LOADING_MIN_HEIGHT);

        PauseTransition delay = new PauseTransition(Duration.millis(50));
        delay.setOnFinished(e -> fillTable());
        delay.play();
    }

    /**
     * Fills the data table with the latest bins
     */
    private void fillTable(){

        tableData.updateData(getState().getBinDAO().getAll());
        table.buildTableContent();

        root.setMinHeight(Region.USE_COMPUTED_SIZE);
    }

    /**
     * Get the table that contains the data
     *
     * @return The data table
     */
    public UITable getTable() {
        return table;
    }
}
