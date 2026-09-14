package seng202.team1.data.status;

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
import seng202.team1.data.User;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinIssue;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.bin.BinStatus;

/**
 * Loads bin report data from a JSON file into {@link StatusReport} instances
 * @author Archie Georgeson
 */
public class StatusReportLoader {
    private static final Logger log = LogManager.getLogger(StatusReportLoader.class);
    private static final String TEST_FILE_PATH = "json/binreports.json";
    private final Path filePath;
    private final List<StatusReport> statusReports = new ArrayList<>();
    private final BinLoader bins;

    /**
     * Instantiates StatusReportLoader
     */
    public StatusReportLoader(BinLoader bins) {
        this(bins, null);
    }

    /**
     * Instantiates StatusReportLoader for use in app
     * @param filePath Path of JSON file for bin reports
     */
    public StatusReportLoader(BinLoader bins, Path filePath) {
        this.bins = bins;
        this.filePath = filePath;
    }

    public List<StatusReport> getStatusReports() {
        return Collections.unmodifiableList(statusReports);
    }

    /**
     * Find StatusReports by a bin ID
     * @param id ID of the bin
     * @return statusReports matching ID, empty if no matching StatusReports
     */
    public List<StatusReport> getStatusReportsById(int id) {
        return statusReports.stream()
                .filter(bin -> bin.getBinID() == id)
                .toList();
    }

    /**
     * Reads JSON file and puts in statusReports list
     * Raises an error if file cannot be read
     */
    public void readJson() {
        statusReports.clear();

        if (filePath != null && Files.notExists(filePath)) {
            return;
        }
        JSONParser parser = new JSONParser();
        try (Reader reader = openReader()){
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray reports = (JSONArray) jsonObject.get("statusReports");
            if (reports == null) {
                return;
            }
            for (Object o : reports) {
                statusReports.add(parseReport((JSONObject) o));
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
        InputStream is = getClass().getResourceAsStream(TEST_FILE_PATH);
        if (is == null) {
            throw new FileNotFoundException("could not find " + TEST_FILE_PATH);
        }
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }

    /**
     * Parses a JSON object into a {@link StatusReport} object.
     *
     * @param jsonObject represents statusReport
     * @return A statusReport object
     */
    private StatusReport parseReport(JSONObject jsonObject) {
        int binID = ((Number) jsonObject.get("binID")).intValue();
        Bin bin = bins.getBinById(binID);
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

        return new StatusReport(bin, user, status, issue, reportDescription, time);
    }
}
