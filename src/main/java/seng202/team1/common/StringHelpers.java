package seng202.team1.common;

import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.Properties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class that provides helpers for common string tasks
 */
public class StringHelpers {

    /**
     * Converts a word to title case
     *
     * @param string The word to convert to title case
     * @return The string with its first letter capitalised and the rest in lower case
     */
    public static String toTitleCaseWord(String string){

        // Cant substring
        if(string.isEmpty())
            return "";

        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static String toTitleCase(String string){

        StringBuilder out = new StringBuilder();

        // Make each word title case
        for(String sub : string.split(" "))
            out.append(toTitleCaseWord(sub)).append(" ");

        return out.toString().trim();
    }

    public static String enumNameToTitle(String string){
        return toTitleCase(string.replace("_", " "));
    }

    public static String toLocaleString(LocalDateTime date){
        return toLocaleString(Instant.from(date.atZone(ZoneId.systemDefault())));

    }

    public static String toLocaleString(Instant date){

        if(date == null)
            return "Unknown Date";

        return DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")
                                .withZone(ZoneId.systemDefault())
                                .format(date);
    }

    public static String roundToNearest(Double value, Integer nearest){

        // Round to nearest multiple of x
        long rounded = Math.round(value / nearest);

        // Covert back to
        return String.format("%d", rounded * nearest);

    }

    public static String featureToAddress(Feature feature){

        Properties properties = feature.properties();

        boolean isAddress = properties.name() == null;

        return isAddress ? String.format("%s %s, %s, %s", properties.housenumber(), properties.street(), properties.city(), properties.postcode())
                         : String.format("%s, %s, %s", properties.name(), properties.city(), properties.county());
    }
}
