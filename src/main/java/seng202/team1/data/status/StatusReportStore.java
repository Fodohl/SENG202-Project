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
     * @param newReport {@link StatusReport} to be added
     * @throws IOException if the JSON file cannot be written
     */
    public void add(StatusReport newReport) throws IOException {
        load();
        Objects.requireNonNull(newReport);
        newReport = checkForDuplicate(newReport);
        List<StatusReport> updatedReports = new ArrayList<>(reports);
        updatedReports.add(newReport);
        writeToJSON(updatedReports);
    }

    /**
     * Checks if a user has reported the bin already, replaces the preexisting issue with the new issue and preexisting
     * status with the new status, unless the new issue or status is null, where it keeps the old one
     * @param newReport {@link StatusReport} to be checked
     * @return a new {@link StatusReport} carrying over the unchanged values of a previous report for a user
     */
    public StatusReport checkForDuplicate(StatusReport newReport) throws IOException {

        BinFullness resultStatus;
        ReportType resultIssue;

        String resultDescription;
        StatusReport existingReport = reports.stream().filter(report -> report.getBinID() == newReport.getBinID()
                        && Objects.equals(report.getSubmittedBy().getName(), newReport.getSubmittedBy().getName())
                        && report.getSubmittedBy().getUserID() == newReport.getSubmittedBy().getUserID()).findFirst().orElse(null);
        if (existingReport != null) {
            if (newReport.getIssue() == null) {
                resultIssue = existingReport.getIssue();
                resultDescription = existingReport.getDescription();
            } else {
                resultIssue = newReport.getIssue();
                resultDescription = newReport.getDescription();
            }
            if (newReport.getFullness() == null) {
                resultStatus = existingReport.getFullness();
            } else {
                resultStatus = newReport.getFullness();
            }
            removeReport(existingReport);
            return new StatusReport(newReport.getBin(), newReport.getSubmittedBy(), resultStatus, resultIssue,
                                    resultDescription, newReport.getSubmittedAt());
        }
        return newReport;
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

        LocalDateTime timestamp = LocalDateTime.now();
        StatusReport entry = new StatusReport(bin, null, fullness, reportType, description, timestamp);

        try{
            add(entry);
        } catch (IOException e) {
            // check fails
            //@Todo log err
        }

    }

}
