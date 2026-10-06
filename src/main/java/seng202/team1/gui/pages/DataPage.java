package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
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

import java.util.Comparator;

/**
 * Page that displays the data used in the app
 */
public class DataPage extends Page {

    /// The container for the whole page
    private VBox root;

    /// The table that contains the app's data
    private UITable table;

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

        // First build
        root = new VBox(20);
        refreshData();

        root.setAlignment(Pos.CENTER);
        return root;
    }

    /**
     * Rerender the table with the latest data
     */
    @Override
    public void updateContents() {
        refreshData();
    }

    /**
     * Refreshes the data shown in the data table
     */
    private void refreshData(){

        // Title
        UITitle pageTitle = new UITitle("Raw Data");
        pageTitle.setAlignment(Pos.CENTER);

        // Data
        StringTableBuilder<Bin> tableData = new StringTableBuilder<Bin>(getState().getBinDAO().getAll())
            .addColumn("binID", "ID")
            .addColumn("fullness", "Fullness", bin -> StringHelpers.enumNameToTitle(bin.getFullness().name()))
            .addColumn("areaName", "Location")
            .addColumn("type", "Type", bin -> StringHelpers.enumNameToTitle(bin.getCategory().name()))
            .sortBy(Comparator.comparing(Bin::getID));

        UIIconButton exportButton = new UIIconButton("/media/images/export.svg", "Export Data");
        exportButton.getStyleClass().add("data-button");

        // Download the table to CSV for exports
        exportButton.setOnMouseClicked(e -> {
            ExportManager.exportToCSV(router.getStage(), ExportManager.exportName("bin_data"), tableData.build());
        });

        table = new UITable(tableData, false);
        table.getStyleClass().add("data-table");
        table.setOnRowClick(row -> getState().goToBin(row.get("ID")));

        // Combine
        root.getChildren().clear();
        root.getChildren().addAll(pageTitle, exportButton, table);

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
