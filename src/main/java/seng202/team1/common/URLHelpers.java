package seng202.team1.common;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class URLHelpers {

    public static String getPage(URI uri){
        return Arrays.stream(uri.getPath().split("/")).toList().getLast();
    }

    public static HashMap<String, String> getParams(URI uri){

        // Setup
        HashMap<String, String> out = new HashMap<>();
        String query = uri.getQuery();

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
