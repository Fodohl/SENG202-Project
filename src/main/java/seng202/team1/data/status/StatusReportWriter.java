package seng202.team1.data.status;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Class for writing to a statusReport Json file
 *
 * @author Archie Georgeson
 */
public class StatusReportWriter {

    /**
     * Writes reports to a file destination in JSON format that {@link StatusReportLoader} can read
     * @param destination Path of JSON file destination
     * @param reports List of {@link StatusReport} to be written to file
     * @throws IOException if the JSON file cannot be written
     */
    public void writeJson(Path destination, List<StatusReport> reports) throws IOException {
        Path absoluteDestination = destination.toAbsolutePath();
        Path parent = absoluteDestination.getParent();
        Files.createDirectories(parent);
        Path temporaryFile = Files.createTempFile(parent, "binreports", ".tmp");

        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
                writer.write(createDocument(reports).toJSONString());
            }

            try {
                Files.move(temporaryFile, absoluteDestination,
                        StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, absoluteDestination, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private JSONObject createDocument(List<StatusReport> reports) {
        JSONArray reportArray = new JSONArray();
        for (StatusReport report : reports) {
            reportArray.add(statusReportToJson(report));
        }

        JSONObject root = new JSONObject();
        root.put("statusReports", reportArray);
        return root;
    }

    private JSONObject statusReportToJson(StatusReport report) {
        JSONObject json = new JSONObject();

        json.put("binID", report.getBinID());
        json.put("user",  report.getSubmittedBy().getName());
        json.put("userID", report.getSubmittedBy().getUserID());
        json.put("fullness", report.getFullness() == null ? null : report.getFullness().toString());
        json.put("issue", report.getIssue() == null ? null : report.getIssue().toString());
        json.put("description", report.getDescription());
        json.put("createdAt", report.getSubmittedAt().toString());

        return json;
    }
}
