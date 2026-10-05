package seng202.team1.unittests.data.table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.types.bin.Bin;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;


public class StringTableTest extends TrashAppTest {

    private StringTable table;
    private StringTableBuilder<Bin> builder;
    private int numRows;


    @BeforeEach
    public void init() {

        // Set up the table
        builder = testDataTable();
        table = builder.build();
        numRows = table.dataSize();
    }

    @Test
    public void headersShouldBeKeys() {
        assertArrayEquals(table.getHeaderRow().toArray(), table.keySet().toArray());
    }

    @Test
    public void sizeShouldBeNumRows() {

        int rowsIncHeader = 1 + numRows;
        assertEquals(rowsIncHeader, table.size());
    }

    @Test
    public void shouldSaftleyReturnEmptyIfOutOfBoundsLower() {
        assertTrue(table.getDataRow(-1).isEmpty());
    }

    @Test
    public void shouldSaftleyReturnEmptyIfOutOfBoundsUpper() {
        assertTrue(table.getDataRow(0xFFFFF).isEmpty());
    }

}