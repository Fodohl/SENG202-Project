package seng202.team1.data.status;


import seng202.team1.data.User;
import seng202.team1.data.bin.*;
import seng202.team1.data.sqlite.DAO.BinDAO;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
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
     *
     * @param filePath of JSON for bin reports
     */
    public StatusReportStore(BinDAO bins, Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.loader = new StatusReportLoader(bins, filePath);
        this.writer = new StatusReportWriter();
    }


    /**
     * Loads all {@link StatusReport} and saves them to reports array
     */
    public void load() {
        loader.readJson();

        reports.clear();
        reports.addAll(loader.getStatusReports());
    }

    public List<StatusReport> getReports() {
        load();
        return List.copyOf(reports);
    }

    public List<StatusReport> getReportsById(int id) {
        load();
        return loader.getStatusReportsById(id);
    }

    /**
     * Adds bin report to JSON file
     *
     * @param newReport {@link StatusReport} to be added
     * @throws IOException if the JSON file cannot be written
     */
    public void add(StatusReport newReport) throws IOException {
        load();
        Objects.requireNonNull(newReport);
        List<StatusReport> updatedReports = new ArrayList<>(reports);
        updatedReports.add(newReport);
        writeToJSON(updatedReports);
    }


    /**
     * Removes a report from JSON file and from report array
     *
     * @param report {@link StatusReport} to be removed
     * @throws IOException if the JSON file cannot be written
     */
    public void removeReport(StatusReport report) throws IOException {
        List<StatusReport> updatedReports = reports.stream().filter(setReport -> !setReport.equals(report)).toList();
        writeToJSON(updatedReports);
    }

    /**
     * Writes to JSON file updated reports
     *
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
     */
    public static Path defaultDataPath() {
        return Path.of(
                System.getProperty("user.home"),
                ".trashapp",
                "statusreports.json"
        );
    }


    public static Path testDataPath() {
        try {

            // Get a temp file on this system
            Path path = Files.createTempFile("trashapp_test_reports", ".json");

            // Empty fails to parse so instead delete the file and let the store recreated it
            Files.delete(path);

            return path;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public void logReport(Bin bin, BinFullness fullness, ReportType reportType, String description) {

        User dummy = new User(10, "null-user");

        LocalDateTime timestamp = LocalDateTime.now();
        StatusReport entry = new StatusReport(bin, dummy, fullness, reportType, description, timestamp);

        try {
            add(entry);
        } catch (IOException e) {
            // check fails
        }

    }

}
