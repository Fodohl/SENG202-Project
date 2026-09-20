package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


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
    private StringTable outData;


    Function<HashMap<String, String>, Boolean> highlighter;

    private String currentSortDataKey = null;
    private boolean sortAscending = false;

    /**
     * Constructs a new UI table
     *
     * @see #UITable(StringTableBuilder, boolean)
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
    public UITable(StringTableBuilder data, boolean highlightFirst) {

        this(data, highlightFirst, null);
    }

     /**
     * Constructs a new UI table with a desired header ordering and highlight rules
     *
     * @param data The data to show in the table
     * @param highlightFirst Whether to highlight the first row or not
     */
    public UITable(StringTableBuilder data, boolean highlightFirst, Function<HashMap<String, String>, Boolean> highlighter){

        this.highlightFirst = highlightFirst;
        this.sourceData = data;
        this.highlighter = highlighter;

        // Style the table
        getStyleClass().add("table");
        setAlignment(Pos.CENTER);

        // Force columns to stretch to container's bounds
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setMaxWidth(Double.MAX_VALUE);

        buildTableContent();
    }

    /**
     * Adds the header row to the table, setting up indices mapping
     *
     * @param headers The header names for each column
     */
    private void setupHeader(Map<String, String> colMap, Set<String> headers) {

       // Setup state
        headerColumns.clear();
        rowWidth = headers.size();

        int i = 0;
        for (String headerDataKey : colMap.keySet()){

            String headerDisplayName = colMap.get(headerDataKey);

            // Add
            addCell(headerCell(headerDataKey, headerDisplayName), true);
            headerColumns.put(headerDisplayName, i);
            i++;
        }
    }

    private HBox headerCell(String dataKey, String displayName) {

        // Style the displayed title
        Label label = new Label(displayName);

        // Indicate that this is the row being sorted
        UIImage sortIcon = new UIImage("/media/images/dir-continue.svg", 20, 20);
        sortIcon.setVisible(currentSortDataKey != null && currentSortDataKey.equals(dataKey));
        sortIcon.setRotate(sortAscending ? 0 : 180);

        // Combine
        HBox headerBox = new HBox(5, label, sortIcon);
        headerBox.setAlignment(Pos.CENTER);

        // Cycle sorting on click
        headerBox.setOnMouseClicked(event -> {
            updateSelectedSort(dataKey);
            buildTableContent();
        });


        return headerBox;

    }

    public void buildTableContent() {

        // Reset state
        getChildren().clear();
        headerColumns.clear();
        currentRow = 0;
        currentCol = 0;

        // Preserve default sorting if yet to manually sort
        if(currentSortDataKey != null)
            sourceData = sourceData.sortBy(currentSortDataKey, sortAscending);

        // Add header row
        outData = sourceData.build();
        setupHeader(outData.getColMap(), outData.getHeaderRow());

        // Add child rows
        for (int i = 0; i < outData.dataSize(); i++)
            addTableRow(outData.getDataRow(i));

    }

    private void updateSelectedSort(String dataKey) {

        // Cycle sorted on subsequent presses
        if(currentSortDataKey != null && currentSortDataKey.equals(dataKey)) {
            sortAscending = !sortAscending;
            return;
        }


        // Sort ascending by default for newly sorted col
        currentSortDataKey = dataKey;
        sortAscending = true;
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
        addCell(label, isHeader);
    }

        /**
         * Add a cell to the table
         *
         * Call when finished with operations on the current working cell
         *
         * @param cell The cell to addRowToTable
         * @param isHeader Whether the cell is in the header row or not
         */
        private void addCell(Node cell, boolean isHeader){

            cell.getStyleClass().add("table-cell");

            // Highlight the header row
            if(isHeader)
                cell.getStyleClass().add("table-cell-header");

            // Style
            roundEdges(cell);
            highlight(cell);

            // Add the cell
            add(cell, currentCol, currentRow);
            nextCell();
    }

    private void highlight(Node cell) {

        // First row gets special highlighting
        if (highlightFirst && currentRow == 1){
            cell.getStyleClass().add("table-cell-highlight-first");
            return;
        }

        // Some rows may be highlighted according to external rules
        if(highlighter == null || currentRow == 0 || !highlighter.apply(outData.getDataRow(currentRow - 1)))
            return;

        cell.getStyleClass().add("table-cell-highlight");

        // Left
        if(currentCol == 0)
            cell.getStyleClass().addAll("cell-round ",  "cell-round-l");

        // Right
        if(currentCol == rowWidth - 1)
            cell.getStyleClass().addAll("cell-round ", "cell-round-r");
    }


    /**
     * Calculate and addRowToTable the required rounding styles for the cell given how it is placed in the table
     *
     * @param cell The cell to style
     */
    private void roundEdges(Node cell){

        // If highlighting a single cell, must bake both values in otherwise will override each-other
        String leftClass = highlightFirst ? "cell-round-tl" : "cell-round-l";
        String rightClass = highlightFirst ? "cell-round-tr" : "cell-round-r";

        // Top left
        if(currentCol == 0 && currentRow == 0)
            cell.getStyleClass().addAll("cell-round ", leftClass);

        // Top right
        if(currentCol == rowWidth - 1 && currentRow == 0)
            cell.getStyleClass().addAll("cell-round ", rightClass);

        // Not styling the first row
        if(!highlightFirst)
            return;

        // Bottom left
        if(currentCol == 0 && currentRow == 1)
            cell.getStyleClass().addAll("cell-round ", "cell-round-bl");

        // Bottom right
        if(currentCol == rowWidth - 1 && currentRow == 1)
            cell.getStyleClass().addAll("cell-round ", "cell-round-br");
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

    public double getActualWidth(){

        double width = 0.0;

        for (int i = 0; i < rowWidth; i++)
            width += this.get(i,0).getBoundsInParent().getWidth();

        return width;

    }
}
