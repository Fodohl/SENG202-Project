package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import seng202.team1.data.StringTable;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static seng202.team1.gui.GraphicsCommon.precentX;

public class UITable extends GridPane {

    //@todo take style config: highlight row function callback

    private HashMap<String, Integer> headerColumns = new HashMap<>();


    private int currentRow = 0;
    private int currentCol = 0;
    private int rowWidth = 0;

    private boolean highlightFirst = true;

    public UITable(StringTable data){
        this(data, data.getHeaderRow().stream().toList(), true);
    }

    public UITable(StringTable data, List<String> headerOrder){
        this(data, headerOrder, true);
    }

    public UITable(StringTable data, List<String> headerOrder, boolean highlightFirst){

        this.highlightFirst = highlightFirst;

        // Style the table
        getStyleClass().add("table");
        setAlignment(Pos.CENTER);

        // Add header row
        setupHeader(headerOrder);

        // Add child rows
        for (int i = 0; i < data.dataSize(); i++)
            addTableRow(data.getDataRow(i));

    }

    private void setupHeader(List<String> headers) {

        headerColumns.clear();
        rowWidth = headers.size();

        for (int i = 0; i < headers.size(); i++){

            // Get the header
            String header = headers.get(i);

            // Add
            addCell(header, true);
            headerColumns.put(header, i);
        }
    }

    private void addTableRow(HashMap<String, String> dataRow){

        for (String header : dataRow.keySet()){

            // Check if this header is displayed
            Integer col = headerColumns.get(header);
            if(col == null)
                return;

            // Add
            String value = dataRow.get(header);
            addCell(value);
        }
    }


    //@todo add row correct num of this call

    private void nextCell(){

        currentCol++;

        // Overflow
        if (currentCol >= rowWidth){
            currentCol = 0;
            currentRow++;
        }

    }

    private void addCell(String value){

        addCell(value, false);
    }

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
}
