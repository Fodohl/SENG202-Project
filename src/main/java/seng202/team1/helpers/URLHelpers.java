package seng202.team1.helpers;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class to help with URL address manipulation
 */
public class URLHelpers {

    /**
     * Get the page portion of a URL
     *
     * @param uri The url to extract from
     * @return The page or empty if no page
     */
    public static String getPage(URI uri){

        // Doesnt have a page
        if(uri.getPath().endsWith("/"))
            return "";

        return Arrays.stream(uri.getPath().split("/")).toList().getLast();
    }

    /**
     * Extract query parameters from a URL
     *
     * @param uri The url to extract from
     * @return The key-value map of parameters or empty if no page
     */
    public static HashMap<String, String> getParams(URI uri){

        // Setup
        HashMap<String, String> out = new HashMap<>();
        String query = uri.getQuery();

        // No params
        if(query == null)
            return out;

        // Parse each param
        for (String param : query.split("&")){
            String[] paramParts = param.split("=");
            out.put(paramParts[0], paramParts[1]);
        }

        // Clean output
        out.remove("_");
        return out;
    }
}
