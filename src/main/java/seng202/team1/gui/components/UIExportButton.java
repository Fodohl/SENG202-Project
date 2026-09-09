package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.core.ExportManager;

/**
 * A UI Component to handle rendering export button
 *
 * Renders a button for exporting a csv of data, expects a svg of the same name (lowercase) to exist for it's icon.
 *
 * Note: No onClick needed, will automatically handle export calls
 */
public class UIExportButton extends HBox {
    
    public UIExportButton(WindowManager windowManager, String iconName) {

        // Setup state
        super();

        // Extract info from page type
        String iconPath = String.format("/images/%s.svg", iconName.toLowerCase());
        String title = StringHelpers.toTitleCase(iconName);

        // Apply styles
        getStyleClass().add("export-button");

        // Add icon
        StackPane icon = new UIImage(iconPath, 25, 25);
        icon.getStyleClass().add("export-icon");

        // Add text
        Label label = new Label(title);
        label.getStyleClass().add("export-text");

        // Combine
        getChildren().addAll(icon, label);

        StringTable stringTable = new StringTableBuilder<Bin>(windowManager.getGlobalState().getDataLoader().getBinObjects()).build();

        // Handle events
        setOnMouseClicked(event -> ExportManager.exportToCSV(windowManager.getStage(), "TrashAppDataExport.csv", stringTable));
    }
}
