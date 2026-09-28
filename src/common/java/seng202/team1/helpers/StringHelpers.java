package seng202.team1.helpers;

import seng202.team1.helpers.DateHelpers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    /**
     * Covert each word in a string to title case
     *
     * @param string The string to convert
     * @return The title-cased string
     */
    public static String toTitleCase(String string){

        StringBuilder out = new StringBuilder();

        // Make each word title case
        for(String sub : string.split(" "))
            out.append(toTitleCaseWord(sub)).append(" ");

        return out.toString().trim();
    }

    /**
     * Converts an enum to a human-readable title
     *
     * @param string The enum constant name
     * @return The enum title
     */
    public static String enumNameToTitle(String string){
        return toTitleCase(string.replace("_", " "));
    }

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
    public static String toLocaleString(Instant date){

        if(date == null)
            return "Unknown Date";

        return DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")
                                .withZone(ZoneId.systemDefault())
                                .format(date);
    }

    /**
     * Print a value rounded to the nearest increment
     *
     * @param value The value to convert
     * @param nearest The nearest multiple
     * @return The rounded string
     */
    public static String roundToNearest(Double value, Integer nearest){

        // Round to nearest multiple of x
        long rounded = Math.round(value / nearest);

        // Covert back to
        return String.format("%d", rounded * nearest);

    }
}
