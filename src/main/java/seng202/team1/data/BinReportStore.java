package seng202.team1.data;

import seng202.team1.models.BinReport;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores bin reports in memory and persists them to a JSON file. Primary class for reading, writing
 * and querying records relating to binReports.
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
    public BinReportStore(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.loader = new BinReportLoader(filePath);
        this.writer = new BinReportWriter();
    }

    /**
     * Instantiates BinReportStore for testing functionality, sets filePath to test JSON file
     */
    public BinReportStore() {
        this.filePath = defaultDataPath();
        this.loader = new BinReportLoader(filePath);
        this.writer = new BinReportWriter();
    }

    /**
     * Returns the default data path
     * @return test JSON file path
     */
    public static Path defaultDataPath() {
        return Path.of(System.getProperty("user.home"),".trashapp","binreports.json");
    }

    /**
     * Loads all binReports and saves them to reports array
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
        return loader.getBinReportsById(id);
    }

    /**
     * Adds bin report to JSON file
     * @param newReport BinReport to be added
     * @throws IOException if the JSON file cannot be written
     */
    public void add(BinReport newReport) throws IOException {
        load();
        Objects.requireNonNull(newReport);
        boolean userReportExistsForBin = reports.stream().anyMatch(
                report -> report.getBinID() == newReport.getBinID()
                && Objects.equals(report.getSubmittedBy().getName(), newReport.getSubmittedBy().getName())
                && report.getSubmittedBy().getUserID() == newReport.getSubmittedBy().getUserID());
        if (userReportExistsForBin) {
            removeReport(newReport);
        }
        List<BinReport> updatedReports = new ArrayList<>(reports);
        updatedReports.add(newReport);
        writeToJSON(updatedReports);
    }

    /**
     * Removes a report from reports if it has the same user and binID
     * @param report to be removed
     * @throws IOException if the JSON file cannot be written
     */
    public void removeReport(BinReport report) throws IOException {
        List<BinReport> updatedReports = reports.stream().filter(setReport -> !((setReport.getBinID()) == report.getBinID()
                && setReport.getSubmittedBy().getName().equals(report.getSubmittedBy().getName()))
                && setReport.getSubmittedBy().getUserID() == report.getSubmittedBy().getUserID()).toList();
        writeToJSON(updatedReports);
    }

    /**
     * Writes to JSON file updated reports
     * @param updatedReports reports to be written to JSON
     * @throws IOException if the JSON file cannot be written
     */
    private void writeToJSON(List<BinReport> updatedReports) throws IOException {
        writer.writeJson(filePath, updatedReports);
        reports.clear();
        reports.addAll(updatedReports);
    }
}
