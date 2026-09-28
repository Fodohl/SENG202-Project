package seng202.team1.data.status;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.impl.QOM;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import seng202.team1.types.User;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;


/**
 * Loads bin report data from a JSON file into {@link StatusReport} instances
 * @author Archie Georgeson
 */
public class StatusReportLoader {
    private static final Logger log = LogManager.getLogger(StatusReportLoader.class);
    protected static final String TEST_FILE_PATH = "/data/example_reports.json";
    private final Path filePath;
    private final List<StatusReport> statusReports = new ArrayList<>();
    private final BinDAO bins;

    /**
     * Instantiates StatusReportLoader
     */
    public StatusReportLoader(BinDAO bins) {
        this(bins, null);
    }

    /**
     * Instantiates StatusReportLoader for use in app
     * @param filePath Path of JSON file for bin reports
     */
    public StatusReportLoader(BinDAO bins, Path filePath) {
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
                .filter(r -> r.getBin() != null)
                .filter(r -> r.getBinID() == id)
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
            log.error("Failed to parse JSON status reports from " + location, e.getMessage());
            throw new IllegalStateException("Failed to parse JSON status reports: " + e.getMessage());
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
        Bin bin = bins.getOne(binID);
        String reportStatus = (String) jsonObject.get("fullness");
        String reportUser = (String) jsonObject.get("user");
        int reportUserID = ((Number) jsonObject.get("id")).intValue();
        String reportIssue = (String) jsonObject.get("issue");
        String reportDescription = (String) jsonObject.get("description");
        User user = new User(reportUserID, Instant.now(), Instant.now(), reportUser, "", "", 0);
        LocalDateTime time = LocalDateTime.parse(jsonObject.get("createdAt").toString());

        BinFullness status;
        if (reportStatus != null) {
            status = switch (reportStatus.toUpperCase(Locale.ROOT)) {
                case "UNKNOWN" -> BinFullness.UNKNOWN;
                case "FULL" -> BinFullness.FULL;
                case "SEMI_FULL" -> BinFullness.SEMI_FULL;
                case "EMPTY" -> BinFullness.EMPTY;
                case "OVERFLOWING" -> BinFullness.OVERFLOWING;
                default ->
                        throw new IllegalStateException("Unexpected value: " + reportStatus.toUpperCase(Locale.ROOT));
            };
        } else {
            status = null;
        }

        ReportType issue;
        if (reportIssue != null) {
            issue = switch (reportIssue.toUpperCase(Locale.ROOT)) {
                case "_FULLNESS_UPDATE" -> ReportType._FULLNESS_UPDATE;
                case "GRAFFITI" -> ReportType.GRAFFITI;
                case "DAMAGED" -> ReportType.DAMAGED;
                case "MISSING" -> ReportType.MISSING;
                case "FIRE_HAZARD" -> ReportType.FIRE_HAZARD;
                case "ILLEGAL_DUMPING" -> ReportType.ILLEGAL_DUMPING;
                case "BLOCKED" -> ReportType.BLOCKED;
                case "OTHER" -> ReportType.OTHER;
                default -> throw new IllegalStateException("Unexpected value: " + reportIssue.toUpperCase(Locale.ROOT));
            };
        } else {
            issue = null;
        }

        return new StatusReport(bin, user, status, issue, reportDescription, time);
    }
}
