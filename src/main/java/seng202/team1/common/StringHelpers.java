package seng202.team1.common;

import java.time.Instant;
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

    public static String toLocaleString(Instant date){

        if(date == null)
            return "Unknown Date";

        return DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a")
                                .withZone(ZoneId.systemDefault())
                                .format(date);
    }
}
