package seng202.team1.data.table;

import java.util.*;
import java.util.function.Function;

/**
 * Builder class for constructing, sorting, and formatting a StringTable.
 */
public class StringTableBuilder<T> {

    private final List<T> rawData;
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