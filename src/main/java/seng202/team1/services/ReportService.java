package seng202.team1.services;

import seng202.team1.data_managers.BinObject;
import seng202.team1.models.BinReport;

/**
 * ReportService
 * Service class used for handling users reports on bin status
 * @author archi
 */
public class ReportService {

    /**
     * Takes a BinReport and processes it
     * @param report BinReport containing binID and BinStatus
     * @return true if successfully processed, false otherwise
     */
    public boolean submitReport(BinReport report) {
        BinObject bin = report.getBin();
        BinObject.BinStatus status = report.getDescription();
        if (!nonNullReport(bin, status)) {
            return false;
        }
        parseBinStatus(bin, status);
        return true;
    }

    /**
     * Verifies status is not null and that the binID supplied exists in the bin dataset
     * @param bin BinObject associated with report
     * @param status BinStatus associated with report
     * @return true if bin exists in dataset and status is not null, false otherwise
     */
    private boolean nonNullReport(BinObject bin, BinObject.BinStatus status) {
        return bin != null && status != null;
    }

    /**
     * Decides whether report is about bin fullness or a report that location is incorrect
     * @param bin BinObject associated with report
     * @param status BinStatus associated with report
     */
    private void parseBinStatus(BinObject bin, BinObject.BinStatus status) {
        if (status == BinObject.BinStatus.INCORRECT_LOCATION) {
            reportIncorrectLocation(bin);
        } else {
            changeBinStatus(bin, status);
        }
    }

    /**
     * Increments the number of incorrect location reports on a BinObject
     * @param bin BinObject associated with report
     */
    private void reportIncorrectLocation(BinObject bin) { bin.incrementNumIncorrectLocations(); }

    /**
     * Alters BinStatus variable of bin to status
     * @param bin BinObject being changed
     * @param status BinStatus changed to
     */
    private void changeBinStatus(BinObject bin, BinObject.BinStatus status) {
        bin.setBinStatus(status);
    }
}
