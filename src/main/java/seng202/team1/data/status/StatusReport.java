package seng202.team1.data.status;

import seng202.team1.data.User;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinFullness;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * StatusReport
 * Class represents a single bin report
 */

public class StatusReport {
    private final int id;
    private final int binId;
    private final User submittedBy;
    private final BinFullness fullness;
    private final ReportType issue;
    private final Instant submittedAt;
    private final String description;

    /**
     * Constructor for StatusReport class
     *
     * @param binId
     * @param submittedBy
     * @param description
     * @param submittedAt
     */

    public StatusReport(int reportId,
                        int binId,
                        User submittedBy,
                        BinFullness fullness,
                        ReportType issue,
                        String description,
                        Instant submittedAt) {
        this.id = reportId;
        this.binId = binId;
        this.submittedBy = submittedBy;
        this.fullness = fullness;
        this.issue = issue;
        this.description = description;
        this.submittedAt = submittedAt;
    }

    public int getId() { return id; }

    /**
     * Get the bin
     * Return the bin
     * @return bin
     */

    public int getBinId() {
        return binId;
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
     * Get the time
     * Return the time
     * @return submittedAt
     */

    public Instant getSubmittedAt() {return submittedAt;}

    /**
     * Get report description
     * Return report description
     * @return description
     */

    public String getDescription() {
        return description;
    }

    public BinFullness getFullness() { return fullness; }

    public ReportType getIssue() { return issue; }

    /**
     * Checks if the supplied {@link StatusReport} matches this {@link StatusReport}
     * @param statusReport to be checked if equal
     * @return a boolean for whether the two StatusReports are equal
     */
    public boolean equals(StatusReport statusReport) {
        return this.getBinId() == statusReport.getBinId()
                && this.getSubmittedBy().getUserID() == statusReport.getSubmittedBy().getUserID()
                && this.getFullness().equals(statusReport.getFullness())
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
                "binID=" + getBinId() +
                ", submittedBy=" + getSubmittedBy() +
                ", fullness=" + getFullness() +
                ", issue=" + getIssue() +
                ", description="+ description +
                ", submittedAt=" + getSubmittedAt() + "}";
    }
}
