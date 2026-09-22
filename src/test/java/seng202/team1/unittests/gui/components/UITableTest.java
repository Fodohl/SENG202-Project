package seng202.team1.unittests.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.Bin;
<<<<<<< HEAD
import seng202.team1.data.table.StringTable;
=======
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
>>>>>>> main
import seng202.team1.gui.components.UITable;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UITableTest extends TrashAppTest {

    private UITable table;
    private ArrayList<Bin> data;
    private StringTable dataTable;
    private StringTableBuilder<Bin> builder;

    @BeforeEach
    public void init(){

        builder = testDataTable();
        dataTable = builder.build();
        data = testData();
        table = new UITable(builder);

    }

    @Test
    public void firstRowContainsOnlyHeader(){

        for (int i = 0; i < dataTable.getHeaderRow().size(); i++){

            // Must have a cell for each header
            Node cell = table.get(i, 0);
            assertNotNull(cell);

            HBox header = (HBox)cell;

            // Cell must be one of header
            String contents = ((Label)header.getChildren().getFirst()).getText();
            assertTrue(dataTable.getHeaderRow().contains(contents));
        }
    }

    @Test
    public void headerLeftIsRounded(){
        List<String> classes = table.get(0,0).getStyleClass();
        assertTrue(classes.contains("cell-round-tl") || classes.contains("cell-round-l"));
    }


    @Test
    public void headerRightIsRounded(){
        List<String> classes = table.get(dataTable.getHeaderRow().size() - 1,0).getStyleClass();
        assertTrue(classes.contains("cell-round-tr") || classes.contains("cell-round-r"));
    }

    @Test
    public void newDataMustAddNewRow(){

        int initalRows = numRows();

        // Rebuild table with added row
        data.add(new Bin(5,5,5,BinCategory.GENERAL_WASTE,  Instant.now(), "test"));
        builder.updateData(data);
        table = new UITable(builder);

        int finalRows = numRows();

        assertEquals(initalRows + 1, finalRows);
    }

    @Test
    public void newDataMustBeOnSingleRow(){

        int placedRow = numRows() + 1;
        Integer binInfo = 5;

        // Rebuild table with added row
        data.add(new Bin(5,binInfo,binInfo, BinCategory.E_WASTE, Instant.now(), "test"));
        builder.updateData(data);
        table = new UITable(builder);


        for (int i = 0; i < dataTable.getHeaderRow().size(); i++) {

            // Must have a cell for each col
            Node cell = table.get(i, placedRow);
            assertNotNull(cell);
        }
    }

    @Test
    public void getCellSafleyReturnsIfCellDoesntExist(){

        assertNull(table.get(dataTable.getHeaderRow().size() + 1000,  0));

    }

     @Test
    public void getCellSafleyReturnsIfInvaildIndex(){

        assertNull(table.get(-1, -2));

    }

    private int numRows(){

        // Count until out of bounds
        int i = 0;
        while (table.get(0,i) != null)
            i++;

        // 0 Indexed
        return i - 1;

    }
}