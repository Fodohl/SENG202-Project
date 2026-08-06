package seng202.team1.models;

import seng202.team1.data_managers.BinObject;

/**
 * BinReport
 * Class represents a single bin report
 */

public class BinReport {
    private final BinObject bin;
    private final User submittedBy;
    private final BinObject.BinStatus description;

    /**
     * Constructor for BinReport class
     * @param bin
     * @param submittedBy
     * @param description
     */

    public BinReport(BinObject bin, User submittedBy, BinObject.BinStatus description) {
        this.bin = bin;
        this.submittedBy = submittedBy;
        this.description = description;
    }

    /**
     * Get the bin
     * Return the bin
     * @return bin
     */

    public BinObject getBin() {
        return bin;
    }

    /**
     * Get the reporter
     * Return the reporter
     * @return submittedBy
     */

    public User getSubmittedBy() {
        return submittedBy;
    }

    /**
     * Get report description
     * Return report description
     * @return description
     */

    public BinObject.BinStatus getDescription() {
        return description;
    }
}
