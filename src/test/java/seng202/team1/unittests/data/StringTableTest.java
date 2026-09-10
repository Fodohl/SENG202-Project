package seng202.team1.unittests.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.data.table.StringTable;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;


public class StringTableTest extends TrashAppTest {

//    private StringTable table;
//
//    private int numRows;
//
//
//    @BeforeEach
//    public void init(){
//
//        // Set up the table
//        table = testDataTable();
//        numRows = table.dataSize();
//    }
//
//    @Test
//    public void headersShouldBeKeys(){
//        assertArrayEquals(table.getHeaderRow().toArray(), table.keySet().toArray());
//    }
//
//     @Test
//    public void sizeShouldBeNumRows(){
//
//        int rowsIncHeader = 1 + numRows;
//        assertEquals(rowsIncHeader, table.size());
//    }
//
//
//    @Test
//    public void shouldSafelyFailIfFieldIsPrivate() throws NoSuchFieldException {
//        assertEquals(false, addPrivate());
//    }
//
//    @Test
//    public void shouldNotAddIfFieldIsPrivate() throws NoSuchFieldException {
//
//        int sizeBefore = table.size();
//        addPrivate();
//        int sizeAfter = table.size();
//
//        assertEquals(sizeBefore, sizeAfter);
//    }
//
//    @Test
//    public void shouldSafelyFailIfFieldDoesntExist() throws NoSuchFieldException {
//         assertEquals(false, addPartial());
//    }
//
//    @Test
//    public void shouldNotAddIfDoesntExist() throws NoSuchFieldException {
//        int sizeBefore = table.size();
//        addPartial();
//        int sizeAfter = table.size();
//
//        assertEquals(sizeBefore, sizeAfter);
//    }
//
//    @Test
//    public void shouldSaftleyReturnEmptyIfOutOfBoundsLower(){
//        assertTrue(table.getDataRow(-1).isEmpty());
//    }
//
//     @Test
//    public void shouldSaftleyReturnEmptyIfOutOfBoundsUpper(){
//        assertTrue(table.getDataRow(0xFFFFF).isEmpty());
//    }
//
//
//    public boolean addPrivate() throws NoSuchFieldException {
//
//         class PrivateTestObj {
//            private double latitude = 4;
//            private double longitude = 4;
//        }
//
//        return table.add(new PrivateTestObj());
//
//    }
//
//    public boolean addPartial() throws NoSuchFieldException {
//
//         class PrivateTestObj {
//            private double latitude = 5;
//        }
//
//        return table.add(new PrivateTestObj());
//
//    }
}