package seng202.team1.data.status;


import seng202.team1.data.User;
import seng202.team1.data.bin.*;
import seng202.team1.data.trashlog.Trash;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for storing bin reports in memory and persisting them to a JSON file. Primary class for reading, writing
 * and querying records relating to {@link StatusReport}.
 *
 * @author Archie Georgeson
 */
public class StatusReportStore {
    private final Path filePath;
    private final StatusReportLoader loader;
    private final StatusReportWriter writer;
    private final List<StatusReport> reports = new ArrayList<>();

    /**
     * Instantiates StatusReportStore for use in app
     * @param filePath of JSON for bin reports
     */
    public StatusReportStore(BinLoader bins, Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.loader = new StatusReportLoader(bins, filePath);
        this.writer = new StatusReportWriter();
    }


    /**
     * Loads all {@link StatusReport} and saves them to reports array
     */
    public void loadStatusReport() {
        loader.readJson();

        reports.clear();
        reports.addAll(loader.getStatusReports());
    }

    public List<StatusReport> getReports() {
        loadStatusReport();
        return List.copyOf(reports);
    }

    public List<StatusReport> getReportsById(int id) {
        loadStatusReport();
        return loader.getStatusReportsById(id);
    }

    /**
     * Gets the last time a bin was reported
     * @Author Connor
     */

    public LocalDateTime getLastReportTime(int binID) {
        loadStatusReport();

        return reports.stream()
                .filter(report -> report.getBin().getID() == binID)
                .map(StatusReport::getSubmittedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    /**
     * Adds bin report to JSON file
     * @param newReport {@link StatusReport} to be added
     * @throws IOException if the JSON file cannot be written
     */
    public void addReportToJson(StatusReport newReport) throws IOException {
        loadStatusReport();
        Objects.requireNonNull(newReport);
        List<StatusReport> updatedReports = new ArrayList<>(reports);
        updatedReports.add(newReport);
        writeToJSON(updatedReports);
    }


    /**
     * Removes a report from JSON file and from report array
     * @param report {@link StatusReport} to be removed
     * @throws IOException if the JSON file cannot be written
     */
    public void removeReport(StatusReport report) throws IOException {
        List<StatusReport> updatedReports = reports.stream().filter(setReport -> !setReport.equals(report)).toList();
        writeToJSON(updatedReports);
    }

    /**
     * Writes to JSON file updated reports
     * @param updatedReports list of {@link StatusReport} to be written to JSON
     * @throws IOException if the JSON file cannot be written
     */
    private void writeToJSON(List<StatusReport> updatedReports) throws IOException {
        writer.writeJson(filePath, updatedReports);
        reports.clear();
        reports.addAll(updatedReports);
    }

    /**
     * Returns the default location for the user's saved trash history.
     *
     * @return default JSON file location
     *
     * @todo subclass this
     */
    public static Path defaultDataPath() {
        return Path.of(
                System.getProperty("user.home"),
                ".trashapp",
                "statusreports.json"
        );
    }


    public void logReport(Bin bin, BinFullness fullness, ReportType reportType, String description){

        User dummy = new User(10, "null-user");

        LocalDateTime timestamp = LocalDateTime.now();
        StatusReport entry = new StatusReport(bin, dummy, fullness, reportType, description, timestamp);

        try{
            addReportToJson(entry);
        } catch (IOException e) {
            // check fails
            //@Todo log err
        }

    }

}
