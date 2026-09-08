package seng202.team1.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import seng202.team1.models.BinReport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class BinReportWriter {
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
        json.put("report", report.getDescription());
        json.put("createdAt", report.getSubmittedAt());

        return json;
    }
}
