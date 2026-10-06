package seng202.team1.types.status;

import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.trashlog.TrashLog;

import java.time.Instant;

/**
 * StatusReport
 * Class represents a single bin report
 */

public class StatusReport {
    private final int id;
    private final int binId;
    private final int userId;
    private final BinFullness fullness;
    private final ReportType issue;
    private final Instant submittedAt;
    private final Instant syncedAt;
    private final String description;

    /**
     * Constructor for StatusReport class
     * @param reportId id of the StatusReport
     * @param binId id of the bin the StatusReport concerns
     * @param userId id of the user who made the report
     * @param fullness enum representing how full the bin is
     * @param issue enum representing the issue with the bin
     * @param description String describing the issue
     * @param submittedAt Instant of when the report was submitted
     * @param syncedAt Instant of when the report was synced to the db
     */

    public StatusReport(int reportId,
                        int binId,
                        int userId,
                        BinFullness fullness,
                        ReportType issue,
                        String description,
                        Instant submittedAt,
                        Instant syncedAt) {
        this.id = reportId;
        this.binId = binId;
        this.userId = userId;
        this.fullness = fullness;
        this.issue = issue;
        this.description = description;
        this.submittedAt = submittedAt;
        this.syncedAt = syncedAt;
    }

    public StatusReport(int binId,
                        int userId,
                        BinFullness fullness,
                        ReportType issue,
                        String description,
                        Instant submittedAt
                        ) {
        this(-1, binId, userId, fullness, issue, description, submittedAt, null);
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
     * Get the user id of the reporter
     * Return the reporter
     * @return userId
     */

    public int getUserId() {
        return userId;
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
                && this.getUserId() == statusReport.getUserId()
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
                ", submittedBy=" + getUserId() +
                ", fullness=" + getFullness() +
                ", issue=" + getIssue() +
                ", description="+ description +
                ", submittedAt=" + getSubmittedAt() + "}";
    }

    /**
     * Get when the report was sycned
     *
     * @return Instant of successfully sync, null otherwise
     */
    public Instant getSyncedAt() {
        return syncedAt;
    }

   public StatusReport asSynced(){
        return new StatusReport(id, binId, userId, fullness, issue, description, submittedAt, Instant.now());
    }
}
