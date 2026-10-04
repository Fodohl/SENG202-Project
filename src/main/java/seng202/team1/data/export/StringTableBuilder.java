package seng202.team1.data.export;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * Builder class for constructing, sorting, and formatting a StringTable.
 */
public class StringTableBuilder<T> {

    /// The data to use to build the table
    private List<T> rawData;

    /// The map of columns to fields (ordered)
    private final Map<String, String> colMap = new LinkedHashMap<>();

    /// The map of fields to data formatters
    private final Map<String, Function<Object, String>> formatters = new HashMap<>();

    /// The comparator that is currently sorting the table
    private Comparator<T> comparator;

    /**
     * Create a new builder instance
     *
     * @param rawData The data to put in the string table
     */
    public StringTableBuilder(List<T> rawData) {
        this.rawData = new ArrayList<>(rawData);
    }

    /**
     * Add a new column to the string table
     *
     * @see #addColumn(String, String, Function)
     */
    public StringTableBuilder<T> addColumn(String dataKey, String displayName) {
        return addColumn(dataKey, displayName, obj -> obj == null ? "" : obj.toString());
    }

    /**
     * Add a new column to the string table
     *
     * @param dataKey The field name on the object
     * @param displayName The name to put as the header
     * @param formatter The function to use to format the value of the field
     * @return The builder instance
     */
    public StringTableBuilder<T> addColumn(String dataKey, String displayName, Function<Object, String> formatter) {
        colMap.put(dataKey, displayName);
        formatters.put(dataKey, formatter);
        return this;
    }

    /**
     * Set the function to use to sort the data of the table
     *
     * @param comparator The sorting function
     * @return The builder instance
     */
    public StringTableBuilder<T> sortBy(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    /**
     * Sort by a specific field
     *
     * Note: Expects templated type to a Comparable, however if not will fall back to string comparison
     *
     * @param dataKey The field to sort by
     * @param ascending Whether to sort ascendingly or descendinly
     * @return The builder instance
     */
    public StringTableBuilder<T> sortBy(String dataKey, boolean ascending) {

        int lessThan = ascending ? -1 : 1;
        int moreThan = ascending ? 1 : -1;

        this.comparator = (left, right) -> {
            try {

                // Get the field of each object
                Field leftField = left.getClass().getDeclaredField(dataKey);
                Field rightField = left.getClass().getDeclaredField(dataKey);

                leftField.setAccessible(true);
                rightField.setAccessible(true);

                // Get each value
                Object leftValue = leftField.get(left);
                Object rightValue = rightField.get(right);

                // Both are null and must be equal
                if (leftValue == null && rightValue == null)
                    return 0;

                if (leftValue == null)
                    return lessThan;

                if (rightValue == null)
                    return moreThan;

                int comparison = 0;
                if (leftValue instanceof Comparable && rightValue instanceof Comparable) {

                    // Use the object's custom comparison logic
                    Comparable<Object> leftCompare = (Comparable<Object>)leftValue;
                    comparison = leftCompare.compareTo(rightValue);

                } else {

                    // Fall back to basic string comparison
                    comparison = leftValue.toString().compareTo(rightValue.toString());
                }

                return ascending ? comparison : -comparison;

            } catch (Exception e) {

                return 0;
            }
        };
        return this;
    }

    /**
     * Refresh the data reference with new data
     *
     * @param newData The new data for the table
     */
    public void updateData(List<T> newData){
        this.rawData = newData;
    }

    /**
     * Turn the data into a string table following the rules set during builder building
     *
     * @return The built string table, sorted
     */
    public StringTable build() {


        if (comparator != null)
            rawData.sort(comparator);

        StringTable table = new StringTable(colMap);
        for (T item : rawData) {
            table.add(item, formatters);
        }

        return table;
    }
}