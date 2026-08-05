package seng202.team1.unittests.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.data.Bin;
import seng202.team1.data.StringTable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class StringTableTest
{

    private StringTable table;

    @BeforeEach
    public void init(){

        // Set up the table
        Map<String, String> keys = Map.of(
                                         "latitude", "lat",
                                        "longitude", "long"
                                        );
        table = new StringTable(keys);

        // Add test data
        table.add(new Bin(1,1,1,null));
        table.add(new Bin(2,2,2, null));
        table.add(new Bin(3,3,3, null));
    }

    @Test
    public void headersShouldBeKeys(){
        assertArrayEquals(table.getHeaderRow().toArray(), table.keySet().toArray());
    }

     @Test
    public void sizeShouldBeNumRows(){

        int numRows = 3;
        int rowsIncHeader = 1 + numRows;

        assertEquals(rowsIncHeader, table.size());
    }


    @Test
    public void shouldSafelyFailIfFieldIsPrivate() throws NoSuchFieldException {
        assertEquals(false, addPrivate());
    }

    @Test
    public void shouldNotAddIfFieldIsPrivate() throws NoSuchFieldException {

        int sizeBefore = table.size();
        addPrivate();
        int sizeAfter = table.size();

        assertEquals(sizeBefore, sizeAfter);
    }

     @Test
    public void shouldSafelyFailIfFieldDoesntExist() throws NoSuchFieldException {
         assertEquals(false, addPartial());
    }

    @Test
    public void shouldNotAddIfDoesntExist() throws NoSuchFieldException {
        int sizeBefore = table.size();
        addPartial();
        int sizeAfter = table.size();

        assertEquals(sizeBefore, sizeAfter);
    }

    public boolean addPrivate() throws NoSuchFieldException {

         class PrivateTestObj {
            private double latitude = 4;
            private double longitude = 4;
        }

        return table.add(new PrivateTestObj());

    }

    public boolean addPartial() throws NoSuchFieldException {

         class PrivateTestObj {
            private double latitude = 5;
        }

        return table.add(new PrivateTestObj());

    }
}