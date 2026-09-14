package seng202.team1.data.status;

import seng202.team1.data.User;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinIssue;
import seng202.team1.data.bin.BinStatus;

import java.time.LocalDateTime;

/**
 * StatusReport
 * Class represents a single bin report
 */

public class StatusReport {
    private final Bin bin;
    private final User submittedBy;
    private final BinStatus status;
    private final BinIssue issue;
    private final LocalDateTime submittedAt;
    private final String description;

    /**
     * Constructor for StatusReport class
     * @param bin
     * @param submittedBy
     * @param description
     * @param submittedAt
     */

    public StatusReport(Bin bin,
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
    public int getBinID() { return bin.getID(); }

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
     * Checks if the supplied {@link StatusReport} matches this {@link StatusReport}
     * @param statusReport to be checked if equal
     * @return a boolean for whether the two StatusReports are equal
     */
    public boolean equals(StatusReport statusReport) {
        return this.getBinID() == statusReport.getBinID()
                && this.getSubmittedBy().getUserID() == statusReport.getSubmittedBy().getUserID()
                && this.getStatus().equals(statusReport.getStatus())
                && this.getIssue().equals(statusReport.getIssue())
                && this.getDescription().equals(statusReport.getDescription());
    }

    /**
     * Returns a string representation of this bin report, including the bin ID,
     * submitter, description, and submission time.
     * @return a string representation of this bin report
     */

    @Override
    public String toString() {
        return "StatusReport{" +
                "binID=" + getBinID() +
                ", submittedBy=" + getSubmittedBy() +
                ", status=" + getStatus() +
                ", issue=" + getIssue() +
                ", description="+ description +
                ", submittedAt=" + getSubmittedAt() + "}";
    }
}
