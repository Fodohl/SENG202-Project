package seng202.team1.data;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class StringTable extends HashMap<String, List<String>> {

    /// Map of object key to table column
    private Map<String, String> colMap;

    /**
     * Construct a new StringTable
     *
     * @param cols A map of data key to display key
     */
    public StringTable(Map<String, String> cols){

        // Store map
        this.colMap = cols;

        // Setup cols
        for(String key : cols.keySet())
            put(key, new ArrayList<>());

    }

    public Set<String> getCols(){
        return keySet();
    }

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
        colMap.forEach((key, value) -> {
            try {

                // Store the value from the object
                Object objectValue = object.getClass().getDeclaredField(key).get(object);
                get(key).add(objectValue.toString());

            } catch (IllegalAccessException | NoSuchFieldException e) {

                // Should never happen as prechecked above
                throw new ArrayIndexOutOfBoundsException("Failed mid object add to StringTable");
            }
        });


        return true;
    }

    @Override
    public int size() {
        return 1 + get(keySet().stream().toList().getFirst()).size();
    }

    //@todo maybe to complicated
}
