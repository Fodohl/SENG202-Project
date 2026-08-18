package seng202.team1.data_managers;

import seng202.team1.models.BinReport;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import seng202.team1.models.User;

/**
 * Loads trash logs data from a JSON file into {@link BinReport} instances
 * @author Archie Georgeson
 */
public class BinReportLoader {
    private static final Logger log = LogManager.getLogger(DataLoader.class);
    private final String filePath = "json/binreports.json";
    private final List<BinReport> binReports = new ArrayList<>();
    private final DataLoader dataLoader = new DataLoader();


    public BinReportLoader() {
        dataLoader.readGeoJSON();
    }

    public List<BinReport> getBinReports() {
        return Collections.unmodifiableList(binReports);
    }

    /**
     * Find BinReports by a bin ID
     * @param id ID of the bin
     * @return binReports matching ID, empty if no matching BinReports
     */
    public List<BinReport> getBinReportsById(int id) {
        return binReports.stream()
                .filter(bin -> bin.getBinID() == id)
                .toList();
    }

    /**
     * Reads JSON file and puts in binReports list
     * Raises an error if file cannot be read
     */
    public void readJson() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = parseJson(parser);
            JSONArray logs = (JSONArray) jsonObject.get("binReports");
            for (Object o : logs) {
                binReports.add(parseLog((JSONObject) o));
            }
        } catch (Exception e) {
            log.error("Failed to parse JSON trash logs from file: " + e.getMessage());
        }
    }

    /**
     * Parses the JSON file into a JSON Object
     * @param parser JSONParser instance used to parse file contents
     * @return {@link JSONObject} with JSON data
     * @throws Exception if an error occurs while reading or parsing JSON file
     */
    private JSONObject parseJson(JSONParser parser) throws Exception {
        InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(filePath);
        if (is != null) {
            return (JSONObject) parser.parse(new InputStreamReader(is));
        }
        return (JSONObject) parser.parse(new FileReader(filePath));
    }

    /**
     * Parses a JSON object into a {@link BinReport} object.
     *
     * @param jsonObject represents binReport
     * @return A binReport object
     */
    private BinReport parseLog(JSONObject jsonObject) {
        String binID = (String) jsonObject.get("binID");
        BinObject bin = dataLoader.getBinById(Integer.parseInt(binID));
        String reportString = (String) jsonObject.get("report");
        String reportUser = (String) jsonObject.get("user");
        String reportUserID = (String) jsonObject.get("userID");
        User user = new User(Integer.parseInt(reportUserID), reportUser);
        LocalDateTime time = LocalDateTime.parse(jsonObject.get("createdAt").toString());

        BinObject.BinStatus report = switch (reportString.toUpperCase(Locale.ROOT)) {
            case "FULL" -> BinObject.BinStatus.FULL;
            case "PARTIAL" -> BinObject.BinStatus.PARTIAL;
            case "EMPTY" -> BinObject.BinStatus.EMPTY;
            case "INCORRECT_LOCATION" -> BinObject.BinStatus.INCORRECT_LOCATION;
            default -> throw new IllegalStateException("Unexpected value: " + reportString.toUpperCase(Locale.ROOT));
        };

        return new BinReport(bin, user, report, time);
    }
}
