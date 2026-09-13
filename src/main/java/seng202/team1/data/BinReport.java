package seng202.team1.data;

import java.time.LocalDateTime;

/**
 * BinReport
 * Class represents a single bin report
 */

public class BinReport {
    private final Bin bin;
    private final User submittedBy;
    private final BinStatus status;
    private final BinIssue issue;
    private final LocalDateTime submittedAt;
    private final String description;

    /**
     * Constructor for BinReport class
     * @param bin
     * @param submittedBy
     * @param description
     * @param submittedAt
     */

    public BinReport(Bin bin,
                     User submittedBy,
                     BinStatus status,
                     BinIssue issue,
                     String description,
                     LocalDateTime submittedAt) {
        this.bin = bin;
        this.submittedBy = submittedBy;
        this.status = status;
        this.issue = issue;
        this.description = description;
        this.submittedAt = submittedAt;
    }

    /**
     * Get the bin
     * Return the bin
     * @return bin
     */

    public Bin getBin() {
        return bin;
    }

    /**
     * Get the bin ID
     * @return binID associated with report
     */
    public int getBinID() { return bin.getBinID(); }

    /**
     * Get the reporter
     * Return the reporter
     * @return submittedBy
     */

    public User getSubmittedBy() {
        return submittedBy;
    }

    /**
     * Get the time
     * Return the time
     * @return submittedAt
     */

    public LocalDateTime getSubmittedAt() {return submittedAt;}

    /**
     * Get report description
     * Return report description
     * @return description
     */

    public String getDescription() {
        return description;
    }

    public BinStatus getStatus() { return status; }

    public BinIssue getIssue() { return issue; }

    /**
     * Checks if the supplied {@link BinReport} matches this {@link BinReport}
     * @param binReport to be checked if equal
     * @return a boolean for whether the two BinReports are equal
     */
    public boolean equals(BinReport binReport) {
        return this.getBinID() == binReport.getBinID()
                && this.getSubmittedBy().getUserID() == binReport.getSubmittedBy().getUserID()
                && this.getStatus().equals(binReport.getStatus())
                && this.getIssue().equals(binReport.getIssue())
                && this.getDescription().equals(binReport.getDescription());
    }

    /**
     * Returns a string representation of this bin report, including the bin ID,
     * submitter, description, and submission time.
     * @return a string representation of this bin report
     */

    @Override
    public String toString() {
        return "BinReport{" +
                "binID=" + getBinID() +
                ", submittedBy=" + getSubmittedBy() +
                ", status=" + getStatus() +
                ", issue=" + getIssue() +
                ", description="+ description +
                ", submittedAt=" + getSubmittedAt() + "}";
    }
}
