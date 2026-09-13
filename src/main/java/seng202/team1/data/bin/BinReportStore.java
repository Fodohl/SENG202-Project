package seng202.team1.data.bin;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for storing bin reports in memory and persisting them to a JSON file. Primary class for reading, writing
 * and querying records relating to {@link BinReport}.
 *
 * @author Archie Georgeson
 */
public class BinReportStore {
    private final Path filePath;
    private final BinReportLoader loader;
    private final BinReportWriter writer;
    private final List<BinReport> reports = new ArrayList<>();

    /**
     * Instantiates BinReportStore for use in app
     * @param filePath of JSON for bin reports
     */
    public BinReportStore(BinLoader bins, Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.loader = new BinReportLoader(bins, filePath);
        this.writer = new BinReportWriter();
    }


    /**
     * Loads all {@link BinReport} and saves them to reports array
     */
    public void load() {
        loader.readJson();

        reports.clear();
        reports.addAll(loader.getBinReports());
    }

    public List<BinReport> getReports() {
        load();
        return List.copyOf(reports);
    }

    public List<BinReport> getReportsById(int id) {
        load();
        return loader.getBinReportsById(id);
    }

    /**
     * Adds bin report to JSON file
     * @param newReport {@link BinReport} to be added
     * @throws IOException if the JSON file cannot be written
     */
    public void add(BinReport newReport) throws IOException {
        load();
        Objects.requireNonNull(newReport);
        newReport = checkForDuplicate(newReport);
        List<BinReport> updatedReports = new ArrayList<>(reports);
        updatedReports.add(newReport);
        writeToJSON(updatedReports);
    }

    /**
     * Checks if a user has reported the bin already, replaces the preexisting issue with the new issue and preexisting
     * status with the new status, unless the new issue or status is null, where it keeps the old one
     * @param newReport {@link BinReport} to be checked
     * @return a new {@link BinReport} carrying over the unchanged values of a previous report for a user
     */
    public BinReport checkForDuplicate(BinReport newReport) throws IOException {
        BinStatus resultStatus;
        BinIssue resultIssue;
        String resultDescription;
        BinReport existingReport = reports.stream().filter(report -> report.getBinID() == newReport.getBinID()
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
            if (newReport.getStatus() == null) {
                resultStatus = existingReport.getStatus();
            } else {
                resultStatus = newReport.getStatus();
            }
            removeReport(existingReport);
            return new BinReport(newReport.getBin(), newReport.getSubmittedBy(), resultStatus, resultIssue,
                                    resultDescription, newReport.getSubmittedAt());
        }
        return newReport;
    }

    /**
     * Removes a report from JSON file and from report array
     * @param report {@link BinReport} to be removed
     * @throws IOException if the JSON file cannot be written
     */
    public void removeReport(BinReport report) throws IOException {
        List<BinReport> updatedReports = reports.stream().filter(setReport -> !setReport.equals(report)).toList();
        writeToJSON(updatedReports);
    }

    /**
     * Writes to JSON file updated reports
     * @param updatedReports list of {@link BinReport} to be written to JSON
     * @throws IOException if the JSON file cannot be written
     */
    private void writeToJSON(List<BinReport> updatedReports) throws IOException {
        writer.writeJson(filePath, updatedReports);
        reports.clear();
        reports.addAll(updatedReports);
    }
}
