package seng202.team1.data.table;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * Builder class for constructing, sorting, and formatting a StringTable.
 */
public class StringTableBuilder<T> {

    private List<T> rawData;
    private final Map<String, String> colMap = new LinkedHashMap<>();
    private final Map<String, Function<Object, String>> formatters = new HashMap<>();
    private Comparator<T> comparator;

    public StringTableBuilder(List<T> rawData) {
        this.rawData = new ArrayList<>(rawData);
    }

    public StringTableBuilder<T> addColumn(String dataKey, String displayName) {
        return addColumn(dataKey, displayName, obj -> obj == null ? "" : obj.toString());
    }

    public StringTableBuilder<T> addColumn(String dataKey, String displayName, Function<Object, String> formatter) {
        colMap.put(dataKey, displayName);
        formatters.put(dataKey, formatter);
        return this;
    }

    public StringTableBuilder<T> sortBy(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    public StringTableBuilder<T> sortBy(String dataKey, boolean ascending) {

        int lessThan = ascending ? -1 : 1;
        int moreThan = ascending ? 1 : -1;

        this.comparator = (left, right) -> {
            try {

                // Get the field of each object
                Field leftFeild = left.getClass().getDeclaredField(dataKey);
                Field rightFeild = left.getClass().getDeclaredField(dataKey);

                leftFeild.setAccessible(true);
                rightFeild.setAccessible(true);

                // Get each value
                Object leftValue = leftFeild.get(left);
                Object rightValue = rightFeild.get(right);

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

                //@todo warn
                return 0;
            }
        };
        return this;
    }

    public void updateData(List<T> newData){
        this.rawData = newData;
    }

    public StringTable build() {


        if (comparator != null)
            rawData.sort(comparator);

        StringTable table = new StringTable(colMap);
        for (T item : rawData) {
            table.addRowToTable(item, formatters);
        }

        return table;
    }
}