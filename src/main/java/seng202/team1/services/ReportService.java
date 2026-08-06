package seng202.team1.services;

import seng202.team1.data_managers.DataLoader;
import seng202.team1.data_managers.BinObject;

public class ReportService {

    private DataLoader dataLoader;


    /**
     * Takes a BinReport and processes it
     * @param report BinReport containing binID and BinStatus
     * @return true if successfully processed, false otherwise
     */
    public boolean submitReport(BinReport report) {
        if (!nonNullReport(report)) {
            return false;
        }
        return true;
    }

    /**
     * Verifies status is not null and that the binID supplied exists in the bin dataset
     * @param report BinReport containing binID and BinStatus
     * @return true if bin exists in dataset and status is not null, false otherwise
     */
    private boolean nonNullReport(BinReport report) {
        BinObject bin = dataLoader.getBinById(report.getBinID);
        BinObject.BinStatus status = report.getStatus;
        return bin != null && status != null;
    }
}
