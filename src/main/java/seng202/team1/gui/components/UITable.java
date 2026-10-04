package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


/**
 * A UI Component to handle rendering tables
 * <p>
 * Takes a given {@link StringTableBuilder} and converts it into a viewable table
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

    /// The data to show in the table
    private StringTableBuilder sourceData;

    /// The currently parsed and computed data
    private StringTable outData;

    /// A function to indicate whether a certain row should be highlighted or not
    Function<HashMap<String, String>, Boolean> highlighter;

    /// Curent key being used to sort the table, if null will used the default one in StringTableBuilder
    private String currentSortDataKey = null;

    /// Whether the data is currently being sorted ascendingly
    private boolean sortAscending = false;

    /**
     * Constructs a new UI table
     *
     * @see #UITable(StringTableBuilder, boolean)
     */
    public UITable(StringTableBuilder data) {
        this(data, true);
    }

    /**
     * Constructs a new UI table with a desired header ordering and highlight rules
     *
     * @param data           The data to show in the table
     * @param highlightFirst Whether to highlight the first row or not
     */
    public UITable(StringTableBuilder data, boolean highlightFirst) {

        this(data, highlightFirst, null);
    }

    /**
     * Constructs a new UI table with a desired header ordering and highlight rules
     *
     * @param data           The data to show in the table
     * @param highlightFirst Whether to highlight the first row or not
     */
    public UITable(StringTableBuilder data, boolean highlightFirst, Function<HashMap<String, String>, Boolean> highlighter) {

        this.highlightFirst = highlightFirst;
        this.sourceData = data;
        this.highlighter = highlighter;

        // Style the table
        getStyleClass().add("table");
        setAlignment(Pos.CENTER);

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

        // Force columns to stretch evenly across the container's full width
        getColumnConstraints().clear();
        for (int col = 0; col < rowWidth; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.setFillWidth(true);
            columnConstraints.setPercentWidth(100.0 / rowWidth);
            getColumnConstraints().add(columnConstraints);
        }

        int i = 0;
        List<Node> headerCells = new java.util.ArrayList<>();
        for (String headerDataKey : colMap.keySet()) {

            String headerDisplayName = colMap.get(headerDataKey);

            // Add
            Node cell = addCell(headerCell(headerDataKey, headerDisplayName), true);
            headerCells.add(cell);
            headerColumns.put(headerDisplayName, i);
            i++;
        }

        attachHeaderHover(headerCells);
    }

    private void attachHeaderHover(List<Node> headerCells) {

        for (Node cell : headerCells) {
            cell.setOnMouseEntered(event -> headerCells.forEach(sibling -> sibling.getStyleClass().add("table-cell-header-hover")));
            cell.setOnMouseExited(event -> headerCells.forEach(sibling -> sibling.getStyleClass().remove("table-cell-header-hover")));
        }
    }

    /**
     * Create a new cell for a header row, makes it clickable to toggle sorting
     *
     * @param dataKey     The key this column represents
     * @param displayName The human readable version of this key
     * @return
     */
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

    /**
     * Turn the contents of the StringTable into UICells
     */
    public void buildTableContent() {

        // Reset state
        getChildren().clear();
        headerColumns.clear();
        currentRow = 0;
        currentCol = 0;

        // Preserve default sorting if yet to manually sort
        if (currentSortDataKey != null)
            sourceData = sourceData.sortBy(currentSortDataKey, sortAscending);

        // Add header row
        outData = sourceData.build();
        setupHeader(outData.getColMap(), outData.getHeaderRow());

        // Add child rows
        for (int i = 0; i < outData.dataSize(); i++)
            addTableRow(outData.getDataRow(i));

    }

    /**
     * Cycles the sorting mode for the give column
     *
     * @param dataKey The col to sort by
     */
    private void updateSelectedSort(String dataKey) {

        // Cycle sorted on subsequent presses
        if (currentSortDataKey != null && currentSortDataKey.equals(dataKey)) {
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
    private void addTableRow(HashMap<String, String> dataRow) {

        // Order the data
        String[] data = new String[dataRow.size()];
        for (String header : dataRow.keySet()) {

            // Check if this header is displayed
            Integer col = headerColumns.get(header);
            if (col == null)
                return;

            // Add to its correct place
            String value = dataRow.get(header);
            data[col] = value;
        }

        // Add, keeping track of every cell placed in this row
        List<Node> rowCells = new java.util.ArrayList<>();
        for (String value : data)
            rowCells.add(addCell(value));

        attachRowHover(rowCells);
    }


    private void attachRowHover(List<Node> rowCells) {

        boolean alreadyHighlighted = rowCells.stream().anyMatch(cell ->
                            cell.getStyleClass().contains("table-cell-highlight")
                        ||  cell.getStyleClass().contains("table-cell-highlight-first"));

        if (alreadyHighlighted)
            return;

        for (Node cell : rowCells) {
            cell.setOnMouseEntered(event ->
                    rowCells.forEach(sibling -> sibling.getStyleClass().add("table-cell-row-hover")));
            cell.setOnMouseExited(event ->
                    rowCells.forEach(sibling -> sibling.getStyleClass().remove("table-cell-row-hover")));
        }
    }

    /**
     * Update current cell state tracking to next valid cell on the table
     */
    private void nextCell() {

        currentCol++;

        // Overflow
        if (currentCol >= rowWidth) {
            currentCol = 0;
            currentRow++;
        }

    }

    /**
     * Add a cell to the table
     *
     * @see #addCell(String, boolean)
     */
    private Node addCell(String value) {

        return addCell(value, false);
    }

    /**
     * Add a cell to the table
     * <p>
     * Call when finished with operations on the current working cell
     *
     * @param value    The text to show in the cell
     * @param isHeader Whether the cell is in the header row or not
     */
    private Node addCell(String value, boolean isHeader) {

        // Create a new cell
        Label label = new Label(value);
        return addCell(label, isHeader);
    }

    /**
     * Add a cell to the table
     * <p>
     * Call when finished with operations on the current working cell
     *
     * @param cell     The cell to add
     * @param isHeader Whether the cell is in the header row or not
     */
    private Node addCell(Node cell, boolean isHeader) {

        cell.getStyleClass().add("table-cell");

        // Highlight the header row
        if (isHeader)
            cell.getStyleClass().add("table-cell-header");

        // Make the cell fill its full grid column/row so its background shows the whole
        GridPane.setHgrow(cell, Priority.ALWAYS);
        GridPane.setVgrow(cell, Priority.ALWAYS);
        GridPane.setFillWidth(cell, true);
        GridPane.setFillHeight(cell, true);
        if (cell instanceof Region) {
            Region region = (Region) cell;
            region.setMaxWidth(Double.MAX_VALUE);
            region.setMaxHeight(Double.MAX_VALUE);
        }

        // Style
        roundEdges(cell);
        highlight(cell);
        stripe(cell);

        // Add the cell
        add(cell, currentCol, currentRow);
        nextCell();

        return cell;
    }

    /**
     * Highlight the give cell if it meets the current highlighting conditions
     *
     * Warning: assumes the cell is in place currentRow,Col
     *
     * @param cell The cell to apply highlighting to
     */
    private void highlight(Node cell) {

        // First row gets special highlighting
        if (highlightFirst && currentRow == 1) {
            cell.getStyleClass().add("table-cell-highlight-first");
            return;
        }

        // Some rows may be highlighted according to external rules
        if (highlighter == null || currentRow == 0 || !highlighter.apply(outData.getDataRow(currentRow - 1)))
            return;

        cell.getStyleClass().add("table-cell-highlight");

        // Left
        if (currentCol == 0)
            cell.getStyleClass().addAll("cell-round ", "cell-round-l");

        // Right
        if (currentCol == rowWidth - 1)
            cell.getStyleClass().addAll("cell-round ", "cell-round-r");
    }

    /**
     * Applies zebra striping to ordinary data rows
     * @param cell The cell to stripe
     */
    private void stripe(Node cell) {

        // Skip the header row
        if (currentRow == 0)
            return;

        // Don't stripe over a highlighted row
        if (cell.getStyleClass().contains("table-cell-highlight") || cell.getStyleClass().contains("table-cell-highlight-first"))
            return;

        // Alternate every other data row
        if (currentRow % 2 == 0)
            cell.getStyleClass().add("table-cell-alt");
    }


    /**
     * Calculate and add the required rounding styles for the cell given how it is placed in the table
     *
     * @param cell The cell to style
     */
    private void roundEdges(Node cell) {

        // If highlighting a single cell, must bake both values in otherwise will override each-other
        String leftClass = highlightFirst ? "cell-round-tl" : "cell-round-l";
        String rightClass = highlightFirst ? "cell-round-tr" : "cell-round-r";

        // Top left
        if (currentCol == 0 && currentRow == 0)
            cell.getStyleClass().addAll("cell-round ", leftClass);

        // Top right
        if (currentCol == rowWidth - 1 && currentRow == 0)
            cell.getStyleClass().addAll("cell-round ", rightClass);

        // Not styling the first row
        if (!highlightFirst)
            return;

        // Bottom left
        if (currentCol == 0 && currentRow == 1)
            cell.getStyleClass().addAll("cell-round ", "cell-round-bl");

        // Bottom right
        if (currentCol == rowWidth - 1 && currentRow == 1)
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

    /**
     * Get the data this table is displaying
     *
     * @return The dataset
     */
    public StringTable getData(){
        return outData;
    }
}
