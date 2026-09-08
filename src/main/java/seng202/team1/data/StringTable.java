package seng202.team1.data;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Stores a list of object values mapped to a column name
 *
 * Waring: Column order is NOT preserved
 * Note: Row order IS preserved
 */
public class StringTable extends HashMap<String, List<String>> {

    /// A map of an object's variable name to a table column header
    private Map<String, String> colMap;

    /**
     * Construct a new StringTable with a given layout
     *
     * @param cols A map of data key to display key
     */
    public StringTable(Map<String, String> cols){

        // Store the map
        this.colMap = cols;

        // Initialise each column with empty row values
        colMap.forEach((data, display) -> {
            put(display, new ArrayList<>());
        });
    }

    /**
     * Get a row of this table containing the data headers
     * ie, the list of display keys
     *
     * @return A list of headers
     */
    public Set<String> getHeaderRow(){
        return keySet();
    }

    /**
     * Get a row of data in the table
     *
     * @param index The index of the row
     * @return The data contained in that row, empty if the row doesn't exist within the bounds of the table
     */
    public HashMap<String, String> getDataRow(int index){

        // Check bounds
        HashMap<String, String> row = new HashMap<>();
        if(index < 0 || index > dataSize())
            return row;

        // Add the relative row from each col
        colMap.forEach((data, display) -> {
            row.put(display, get(display).get(index));
        });

        return row;
    }

    /**
     * Try to add a row to the table. A row will fail to add if it does not contain a variable for specified key, or if
     * the variable is inaccessible.
     *
     * Note: If data failed to add, table will remain unaltered.
     * Note: Data does not have be a consistent type, just must conform to the specified headers.
     *
     * @param object The data for the row
     * @return True if the row was added successfully, false otherwise.
     */
    public boolean add(Object object){

        // Get fields the object has
        List<Field> declearedFields = Arrays.asList(object.getClass().getDeclaredFields());

        // Map field name to field
        HashMap<String, Field> fields = new HashMap<>();
        for(Field field : declearedFields)
            fields.put(field.getName(), field);

        // Validate that object has required keys and that they are accessible
        for(String key : colMap.keySet()){

            Field field = fields.get(key);

            // Doesnt have value
            if(field == null)
                return false;

            // Value is private
            if(Modifier.isPrivate(field.getModifiers()))
                return false;

        }

        // Add each value to the table
        colMap.forEach((data, display) -> {
            try {

                // Store the value from the object
                Object objectValue = object.getClass().getDeclaredField(data).get(object);
                get(display).add(objectValue.toString());

            } catch (IllegalAccessException | NoSuchFieldException e) {

                // Should never happen as prechecked above, unrecoverable as data may have been added here
                throw new RuntimeException(String.format("Failed mid object add to StringTable: %s", e.getMessage()));
            }
        });

        // All fields must have been added
        return true;
    }

    /**
     * Get the total size of the table. Total size is the number of rows (including the header row)
     *
     * @return The size of the table
     */
    @Override
    public int size() {
        return 1 + dataSize();
    }

     /**
     * Get the size of the data table. Data size is the number of rows excluding the header row
     *
     * @return The size of the table's data
     */
    public int dataSize() {
        return get(keySet().stream().toList().getFirst()).size();
    }
}