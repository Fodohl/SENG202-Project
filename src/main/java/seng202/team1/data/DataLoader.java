package seng202.team1.data;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * DataLoader
 * Loads bin data from a GeoJSON file into Bin instances
 *
 * @author Stanley Easthope
 */
public class DataLoader {

    /// DataLoader specific logger
    private static final Logger log = LogManager.getLogger(DataLoader.class);

    /// Where to load the bins from
    private static final String BIN_DATA = "geojson/bin.geojson";

    /// The list of bins loaded
    private final List<Bin> binObjects = new ArrayList<>();

    /**
     * Get loaded bins list
     *
     * @return List of Bin instances
     */
    public List<Bin> getBinObjects() {
        return Collections.unmodifiableList(binObjects);
    }

    /**
     * Converts the bin data to a string table for displaying
     */
    public StringTable toStringTable(){

        // Set up the table
        Map<String, String> keys = Map.of(
                                        "binID", "ID",
                                        "latitude", "Latitude",
                                        "longitude","Longitude",
                                        "type", "Type"
                                        );
        StringTable out = new StringTable(keys);


        // Add all binds
        for (Bin bin : binObjects)
            out.add(bin);

        return out;
    }

    /**
     * Find bin by  ID
     *
     * @param binID ID of the bin
     * @return The Bin or Null if not found
     */
    public Bin getBinById(int binID) {
        return binObjects.stream()
                .filter(bin -> bin.getID() == binID)
                .findFirst()
                .orElse(null);
    }


    /**
     * Reads the GeoJSON file and puts it in a binObjects list
     * Logs an error if the file can't be read
     */
    public void readGeoJSON(){
        JSONParser parser = new JSONParser();
        try {

            // Load the whole data file
            JSONObject geoObj = parseGeoJson(parser);
            JSONArray features = (JSONArray) geoObj.get("features");

            // Each bin is stored as a feature
            for (Object obj : features) {
                binObjects.add(parseFeature((JSONObject) obj));
            }
        } catch (Exception e) {
            log.error("Failed to read GeoJSON file: " + e.getMessage(), e);
        }
    }

    /**
     * Parses the GeoJSON file into a JSONObject
     *
     * @param parser - JSONParser instance
     * @return JSONObject with the GeoJSON data
     * @throws Exception
     */
    private JSONObject parseGeoJson(JSONParser parser) throws Exception {
        InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(BIN_DATA);
        if (is != null) {
            return (JSONObject) parser.parse(new InputStreamReader(is));
        }
        return (JSONObject) parser.parse(new FileReader(BIN_DATA));
    }

    /**
     * Parses a feature from the GeoJSON data into a BinObject
     *
     * @param feature - JSONObject representing a feature
     * @return Bin instance
     */
    private Bin parseFeature(JSONObject feature) {

        // Get the location data of the in
        JSONObject geometry = (JSONObject) feature.get("geometry");
        JSONArray coordinates = (JSONArray) geometry.get("coordinates");

        double longitude = ((Number) coordinates.get(0)).doubleValue();
        double latitude = ((Number) coordinates.get(1)).doubleValue();

        // Get the bin info
        JSONObject properties = (JSONObject) feature.get("properties");
        int binID = ((Number) properties.get("BinID")).intValue();
        Object wasteTypeObj = properties.get("WasteType");
        Object lastEditDateObj = properties.get("LastEditDate");
        String area = properties.get("SiteName").toString().split(" - ")[1];

        // Parse the wast info
        String wasteType = wasteTypeObj == null ? "" : wasteTypeObj.toString();
        WasteType binType = switch (wasteType.toUpperCase(Locale.ROOT)) {
            case "RECYCLE", "RECYCLING" -> WasteType.RECYCLING;
            case "COMPOST" -> WasteType.ORGANIC;
            default -> WasteType.GENERAL_WASTE;
        };

        // Parse the date
        Instant updated = lastEditDateObj != null ? Instant.parse(lastEditDateObj.toString()) : null;

        return new Bin(binID, latitude, longitude, binType, updated, area);
    }
    
}
