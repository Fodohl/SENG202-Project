package seng202.team1.data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

/**
 * Loads bin report data from a JSON file into {@link BinReport} instances
 * @author Archie Georgeson
 */
public class BinReportLoader {
    private static final Logger log = LogManager.getLogger(BinReportLoader.class);
    private static final String TEST_FILE_PATH = "json/binreports.json";
    private final Path filePath;
    private final List<BinReport> binReports = new ArrayList<>();
    private final DataLoader dataLoader = new DataLoader();

    /**
     * Instantiates BinReportLoader for use in app
     * @param filePath Path of JSON file for bin reports
     */
    public BinReportLoader(Path filePath) {
        dataLoader.readGeoJSON();
        this.filePath = filePath;
    }

    /**
     * Instantiates BinReportLoader for use in testing
     */
    public BinReportLoader() {
        dataLoader.readGeoJSON();
        this.filePath = null;
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
        binReports.clear();

        if (filePath != null && Files.notExists(filePath)) {
            return;
        }
        JSONParser parser = new JSONParser();
        try (Reader reader = openReader()){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray reports = (JSONArray) jsonObject.get("binReports");
            if (reports == null) {
                return;
            }
            for (Object o : reports) {
                binReports.add(parseReport((JSONObject) o));
            }
        } catch (Exception e) {
            String location = filePath == null ? TEST_FILE_PATH : filePath.toString();
            log.error("Failed to parse JSON bin reports from " + location, e.getMessage());
            throw new IllegalStateException("Failed to parse JSON bin reports: " + e.getMessage());
        }
    }

    private Reader openReader() throws IOException {
        if (filePath != null) {
            return Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
        }
        InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(TEST_FILE_PATH);
        if (is == null) {
            throw new FileNotFoundException("could not find " + TEST_FILE_PATH);
        }
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }

    /**
     * Parses a JSON object into a {@link BinReport} object.
     *
     * @param jsonObject represents binReport
     * @return A binReport object
     */
    private BinReport parseReport(JSONObject jsonObject) {
        int binID = ((Number) jsonObject.get("binID")).intValue();
        Bin bin = dataLoader.getBinById(binID);
        String reportStatus = (String) jsonObject.get("status");
        String reportUser = (String) jsonObject.get("user");
        int reportUserID = ((Number) jsonObject.get("userID")).intValue();
        String reportIssue = (String) jsonObject.get("issue");
        String reportDescription = (String) jsonObject.get("description");
        User user = new User(reportUserID, reportUser);
        LocalDateTime time = LocalDateTime.parse(jsonObject.get("createdAt").toString());

        BinStatus status;
        if (reportStatus != null) {
            status = switch (reportStatus.toUpperCase(Locale.ROOT)) {
                case "FULL" -> BinStatus.FULL;
                case "PARTIAL" -> BinStatus.PARTIAL;
                case "EMPTY" -> BinStatus.EMPTY;
                case "OVERFLOWING" -> BinStatus.OVERFLOWING;
                default ->
                        throw new IllegalStateException("Unexpected value: " + reportStatus.toUpperCase(Locale.ROOT));
            };
        } else {
            status = null;
        }

        BinIssue issue;
        if (reportIssue != null) {
            issue = switch (reportIssue.toUpperCase(Locale.ROOT)) {
                case "GRAFFITI" -> BinIssue.GRAFFITI;
                case "DAMAGED" -> BinIssue.DAMAGED;
                case "MISSING" -> BinIssue.MISSING;
                case "FIRE_HAZARD" -> BinIssue.FIRE_HAZARD;
                case "ILLEGAL_DUMPING" -> BinIssue.ILLEGAL_DUMPING;
                case "BLOCKED" -> BinIssue.BLOCKED;
                case "OTHER" -> BinIssue.OTHER;
                default -> throw new IllegalStateException("Unexpected value: " + reportIssue.toUpperCase(Locale.ROOT));
            };
        } else {
            issue = null;
        }

        return new BinReport(bin, user, status, issue, reportDescription, time);
    }
}
