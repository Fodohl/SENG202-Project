package seng202.team1.data;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringTableTestEntry{

    String name;
    int value;

    public StringTableTestEntry(String name, int value){
        this.name = name;
        this.value = value;
    }

}

public class StringTableTest
{

    private StringTable table;

    @BeforeEach
    public void init(){

        // Set up the table
        Map<String, String> keys = Map.of(
                                        "Name", "name",
                                        "Value", "val"
                                        );
        table = new StringTable(keys);

        // Add test data
        table.add(new StringTableTestEntry("One", 1));
        table.add(new StringTableTestEntry("Two", 2));
        table.add(new StringTableTestEntry("Three", 3));
    }

    @Test
    public void headersShouldBeKeys(){
        assertArrayEquals(table.getCols().toArray(), table.keySet().toArray());
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
    public void shouldSafelyFailIfFieldDoesntExist(){

    }

    @Test
    public void shouldNotAddIfDoesntExist(){

    }

    public boolean addPrivate() throws NoSuchFieldException {

        StringTableTestEntry test = new StringTableTestEntry("Four", 4);
        test.getClass().getDeclaredField("Name").setAccessible(false);

        return table.add(test);

    }

}