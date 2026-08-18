package seng202.team1.gui.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.StringTable;

/**
 * Manages exporting data to csv
 */
public class ExportManager {

    private static final Logger log = LogManager.getLogger(ExportManager.class);

    /**
     * Prompts the user with a save dialog, then writes the given table to disk as a csv file.
     *
     * Note: does nothing if the user cancels the save dialog.
     *
     * @param stage The window to anchor the save dialog to
     * @param fileName The suggested file name for the export
     * @param data The table of data to export
     */
    public static void exportToCSV(Stage stage, String fileName, StringTable data) {

        // Ask the user where to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export CSV");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(stage);
        if (file == null)
            return;

        // Write the table to the chosen file
        List<String> headers = data.getHeaderRow().stream().toList();
        try (FileWriter writer = new FileWriter(file)) {

            writer.write(toCSVRow(headers));

            for (int i = 0; i < data.dataSize(); i++) {
                Map<String, String> row = data.getDataRow(i);
                writer.write(toCSVRow(headers.stream().map(row::get).toList()));
            }

        } catch (IOException e) {
            log.error("Failed to export data to csv: {}", e.getMessage());
        }
    }

    /**
     * Converts a row of values into a single escaped csv line
     *
     * @param values The values to write
     * @return The csv formatted line, including trailing newline
     */
    private static String toCSVRow(List<String> values) {
        return String.join(",", values.stream().map(ExportManager::escapeCSVValue).toList()) + "\n";
    }

    /**
     * Escapes a single value for safe inclusion in a csv file
     *
     * @param value The raw value
     * @return The escaped value
     */
    private static String escapeCSVValue(String value) {
        if (value == null)
            return "";

        if (value.contains(",") || value.contains("\"") || value.contains("\n"))
            return "\"" + value.replace("\"", "\"\"") + "\"";

        return value;
    }

}
