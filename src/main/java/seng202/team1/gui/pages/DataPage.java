package seng202.team1.gui.pages;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.gui.components.UIExportButton;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.core.WindowManager;

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

        super(windowManager, PageType.DASHBOARD);

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

        // Export button
        UIExportButton exportButton = new UIExportButton(windowManager, "export");

        // Data
        List<String> order = List.of("ID", "Latitude", "Longitude", "Type");
        UITable table = new UITable(windowManager.getGlobalState().getDataLoader().toStringTable(), order, false);

        // Combine
        VBox root = new VBox(20, pageTitle, exportButton, table);
        root.setAlignment(Pos.CENTER);
        return root;
    }
}
