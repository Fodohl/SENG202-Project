package seng202.team1.common;

import seng202.team1.api.photon.Feature;
import seng202.team1.api.photon.Properties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
<<<<<<< HEAD
=======
import java.util.Date;
>>>>>>> main

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

<<<<<<< HEAD
=======
    /**
     * Covert each word in a string to title case
     *
     * @param string The string to convert
     * @return The title-cased string
     */
>>>>>>> main
    public static String toTitleCase(String string){

        StringBuilder out = new StringBuilder();

        // Make each word title case
        for(String sub : string.split(" "))
            out.append(toTitleCaseWord(sub)).append(" ");

        return out.toString().trim();
    }

<<<<<<< HEAD
=======
    /**
     * Converts an enum to a human-readable title
     *
     * @param string The enum constant name
     * @return The enum title
     */
>>>>>>> main
    public static String enumNameToTitle(String string){
        return toTitleCase(string.replace("_", " "));
    }

<<<<<<< HEAD
    public static String toLocaleString(LocalDateTime date){
        return toLocaleString(Instant.from(date.atZone(ZoneId.systemDefault())));

    }

=======
    /**
     * Print a local time in the format 'dd MMM yyyy, h:mm a'
     *
     * @param date The local time
     * @return The converted date string
     */
    public static String toLocaleString(LocalDateTime date){
        return toLocaleString(DateHelpers.fromLocalTime(date));
    }

    /**
     * Print a instant in the format "dd MMM yyyy, h:mm a" or "Unknown" if null
     *
     * @param date The instant
     * @return The converted date string
     */
>>>>>>> main
    public static String toLocaleString(Instant date){

        if(date == null)
            return "Unknown Date";

        return DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")
                                .withZone(ZoneId.systemDefault())
                                .format(date);
    }

<<<<<<< HEAD
=======
    /**
     * Print a value rounded to the nearest increment
     *
     * @param value The value to convert
     * @param nearest The nearest multiple
     * @return The rounded string
     */
>>>>>>> main
    public static String roundToNearest(Double value, Integer nearest){

        // Round to nearest multiple of x
        long rounded = Math.round(value / nearest);

        // Covert back to
        return String.format("%d", rounded * nearest);

    }

<<<<<<< HEAD
=======
    /**
     * Convert a photon location feature to a readable address
     *
     * @param feature The location
     * @return The assembled address
     */
>>>>>>> main
    public static String featureToAddress(Feature feature){

        Properties properties = feature.properties();

        boolean isAddress = properties.name() == null;

        return isAddress ? String.format("%s %s, %s, %s", properties.housenumber(), properties.street(), properties.city(), properties.postcode())
                         : String.format("%s, %s, %s", properties.name(), properties.city(), properties.county());
    }
}
