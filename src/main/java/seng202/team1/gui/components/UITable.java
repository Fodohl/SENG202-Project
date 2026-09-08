package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * A UI Component to handle rendering tables
 *
 * Takes a given {@link table} and converts it into a viewable table
 */
public class UITable extends GridPane {

    /// Maps a header to its respective column index
    private HashMap<String, Integer> headerColumns = new HashMap<>();

    /// The row that the next cell should be added to
    private int currentRow = 0;

    /// The column that the next cell should be added to
    private int currentCol = 0;

    /// How many columns can be in a single row
    private int rowWidth = 0;

    /// Whether to highlight the first data row or not
    private boolean highlightFirst = true;

    private StringTableBuilder sourceData;

    /**
     * Constructs a new UI table
     *
     * @see #UITable(StringTable, boolean)
     */
    public UITable(StringTableBuilder data){
        this(data, true);
    }

    /**
     * Constructs a new UI table with a desired header ordering and highlight rules
     *
     * @param data The data to show in the table
     * @param highlightFirst Whether to highlight the first row or not
     */
    public UITable(StringTableBuilder data, boolean highlightFirst){

        this.highlightFirst = highlightFirst;
        this.sourceData = data;

        // Style the table
        getStyleClass().add("table");
        setAlignment(Pos.CENTER);

        // Add header row
        StringTable outData = sourceData.build();
        setupHeader(outData.getHeaderRow());

        // Add child rows
        for (int i = 0; i < outData.dataSize(); i++)
            addTableRow(outData.getDataRow(i));
    }

    /**
     * Adds the header row to the table, setting up indices mapping
     *
     * @param headers The header names for each column
     */
    private void setupHeader(Set<String> headers) {

       // Setup state
        headerColumns.clear();
        rowWidth = headers.size();

        int i = 0;
        for (String header : headers) {

            // Add
            addCell(header, true);
            headerColumns.put(header, i);
            i++;
        }
    }

    /**
     * Adds a row of data to the table, placing cells in the desired order
     *
     * @param dataRow The data to display
     */
    private void addTableRow(HashMap<String, String> dataRow){

        // Order the data
        String[] data = new String[dataRow.size()];
        for (String header : dataRow.keySet()){

            // Check if this header is displayed
            Integer col = headerColumns.get(header);
            if(col == null)
                return;

            // Add to its correct place
            String value = dataRow.get(header);
            data[col] = value;
        }

        // Add
        for (String value : data)
          addCell(value);
    }

    /**
     * Update current cell state tracking to next valid cell on the table
     */
    private void nextCell(){

        currentCol++;

        // Overflow
        if (currentCol >= rowWidth){
            currentCol = 0;
            currentRow++;
        }

    }

    /**
     * Add a cell to the table
     * 
     * @see #addCell(String, boolean)
     */
    private void addCell(String value){

        addCell(value, false);
    }

    /**
     * Add a cell to the table
     *
     * Call when finished with operations on the current working cell
     * 
     * @param value The text to show in the cell
     * @param isHeader Whether the cell is in the header row or not
     */
    private void addCell(String value, boolean isHeader){

        // Create a new cell
        Label label = new Label(value);
        label.getStyleClass().add("table-cell");

        // Highlight the header row
        if(isHeader)
            label.getStyleClass().add("table-cell-header");

        // Style
        roundEdges(label);
        //highlight(label);

        // Add the cell
        add(label, currentCol, currentRow);
        nextCell();
    }

    /**
     * Calculate and add the required rounding styles for the cell given how it is placed in the table
     *
     * @param cell The cell to style
     */
    private void roundEdges(Node cell){

        // If highlighting a single cell, must bake both values in otherwise will override each-other
        String leftClass = highlightFirst ? "cell-round-tl" : "cell-round-l";
        String rightClass = highlightFirst ? "cell-round-tr" : "cell-round-r";

        // Top left
        if(currentCol == 0 && currentRow == 0){
            cell.getStyleClass().addAll("cell-round ", leftClass);
        }

        // Top right
        if(currentCol == rowWidth - 1 && currentRow == 0){
            cell.getStyleClass().addAll("cell-round ", rightClass);
        }

        // Not styling the first row
        if(highlightFirst)
            return;

        // Bottom left
        if(currentCol == 0 && currentRow == 1){
            cell.getStyleClass().addAll("cell-round ", "cell-round-bl");
        }

        // Bottom right
        if(currentCol == rowWidth - 1 && currentRow == 1){
            cell.getStyleClass().addAll("cell-round ", "cell-round-br");
        }
    }

    /**
     * Helper method to fetch the contents of a cell in the table
     *
     * @param col The column that the cell is in
     * @param row THe row that the cell is in
     * @return The cell, or null if col/row out of bounds
     */
    public Node get(int col, int row) {

        for (Node node : getChildren())
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row)
                return node;

        return null;
    }
}
