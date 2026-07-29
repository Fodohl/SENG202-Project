package seng202.team0.data_managers;

import org.json.simple.parser.*;
import org.json.simple.*;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class DataLoader {
    private final String filePath = "GeoJson/bin.geojson";
    public ArrayList<BinObject> binObjects = new ArrayList<>();
    public void readGeoJSON(){
        JSONParser parser = new JSONParser();
        try {
            java.io.InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(filePath);
            JSONObject geoObj;
            if (is != null) {
                geoObj = (JSONObject) parser.parse(new InputStreamReader(is));
            } else {
                geoObj = (JSONObject) parser.parse(new java.io.FileReader(filePath));
            }
            JSONArray features = (JSONArray) geoObj.get("features");
            for (Object obj : features) {
                JSONObject feature = (JSONObject) obj;
                JSONObject geometry = (JSONObject) feature.get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                double longitude = (double) coordinates.get(0);
                double latitude = (double) coordinates.get(1);
                JSONObject properties = (JSONObject) feature.get("properties");
                Object wasteTypeObj = properties.get("WasteType");
                String wasteType = wasteTypeObj == null ? "" : wasteTypeObj.toString();
                BinObject.BinType binType = switch (wasteType.toUpperCase(Locale.ROOT)) {
                    case "RECYCLE", "RECYCLING" -> BinObject.BinType.RECYCLING;
                    case "COMPOST" -> BinObject.BinType.ORGANIC;
                    default -> BinObject.BinType.GENERAL_WASTE;
                };
                binObjects.add(new BinObject(binObjects.size() + 1, longitude, latitude, binType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
