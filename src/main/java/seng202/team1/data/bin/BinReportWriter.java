package seng202.team1.data.bin;

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
 * Class for writing to a binReport Json file
 *
 * @author Archie Georgeson
 */
public class BinReportWriter {

    /**
     * Writes reports to a file destination in JSON format that {@link BinReportLoader} can read
     * @param destination Path of JSON file destination
     * @param reports List of {@link BinReport} to be written to file
     * @throws IOException if the JSON file cannot be written
     */
    public void writeJson(Path destination, List<BinReport> reports) throws IOException {
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

    private JSONObject createDocument(List<BinReport> reports) {
        JSONArray reportArray = new JSONArray();
        for (BinReport report : reports) {
            reportArray.add(binReportToJson(report));
        }

        JSONObject root = new JSONObject();
        root.put("binReports", reportArray);
        return root;
    }

    private JSONObject binReportToJson(BinReport report) {
        JSONObject json = new JSONObject();

        json.put("binID", report.getBinID());
        json.put("user",  report.getSubmittedBy().getName());
        json.put("userID", report.getSubmittedBy().getUserID());
        json.put("status", report.getStatus() == null ? null : report.getStatus().toString());
        json.put("issue", report.getIssue() == null ? null : report.getIssue().toString());
        json.put("description", report.getDescription());
        json.put("createdAt", report.getSubmittedAt().toString());

        return json;
    }
}
