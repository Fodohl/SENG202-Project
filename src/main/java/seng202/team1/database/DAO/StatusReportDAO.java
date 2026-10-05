package seng202.team1.database.DAO;

import seng202.team1.database.DatabaseManager;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Database Access Object that handles all {@link StatusReport} related actions to the database
 *
 * @author Archie Georgeson
 */
public class StatusReportDAO extends BaseDAO<StatusReport> {

    /**
     * Instantiates StatusReportDAO and gets a reference to the database singleton
     * @param databaseManager database singleton
     */
    public StatusReportDAO(DatabaseManager databaseManager) {
        super(databaseManager);
    }


    /**
     * Converts the string representation saved in the database to a {@link BinFullness}
     * @param binFullnessString String representation of bin fullness
     * @return BinFullness associated with the report
     */
    private BinFullness convertStringToBinFullness(String binFullnessString) {
        try {
            return BinFullness.valueOf(binFullnessString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected value: " + binFullnessString, e);
        }
    }

    @Override
    protected StatusReport fromRow(ResultSet rs) throws SQLException {
        return new StatusReport(
                rs.getInt("id"),
                rs.getInt("bin_id"),
                rs.getInt("user_id"),
                convertStringToBinFullness(rs.getString("fullness")),
                convertStringToIssue(rs.getString("issue")),
                rs.getString("description"),
                getInstant(rs, "created_at"));
    }

    /**
     * Gets all status reports from the database
     * @return a list of all status reports
     */
    @Override
    public List<StatusReport> getAll() {
        return queryList("SELECT * FROM status_reports");
    }

    /**
     * Gets a status report related to a certain report id
     * @param id id of object to get
     * @return Status report related to id, or null if none
     */
    @Override
    public StatusReport getOne(int id) {
        return queryOne("SELECT * FROM status_reports WHERE id = ?", id);
    }

    /**
     * Gets all reports relating to a bin id
     * @param binId id of the desired bin
     * @return a list of StatusReports relating to the supplied bin id
     */
    public List<StatusReport> getReportsByBinId(int binId) {
        return queryList("SELECT * FROM status_reports WHERE bin_id = ?", binId);
    }

    /**
     * Gets all reports relating to a user
     * @param userId id of the desired user
     * @return a list of StatusReports relating to the supplied user id
     */
    public List<StatusReport> getReportsByUserId(int userId) {
        return queryList("SELECT * FROM status_reports WHERE user_id = ?", userId);
    }

    /**
     * Gets all fullness reports relating to a certain bin
     * @param binId id of the desired bin
     * @return a list of fullness StatusReports relating to the supplied bin
     */
    public List<StatusReport> getFullnessReportsByBinId(int binId) {
        return queryList("SELECT * FROM status_reports WHERE bin_id = ? AND issue = ?",
                binId, ReportType._FULLNESS_UPDATE);
    }

    /**
     * Gets all non fullness reports
     * @return A list of StatusReports with issue other that ReportType._FULLNESS_UPDATE
     */
    public List<StatusReport> getNonFullnessReports() {
        return queryList("SELECT * FROM status_reports WHERE issue IS NOT ? COLLATE NOCASE", ReportType._FULLNESS_UPDATE);
    }

    /**
     * Returns the fullness saved in StatusReport for a fullness update caused by a trash log
     * @param trashLog TrashLog that we want the fullness for
     * @return BinFullness related to the trashLog
     */
    public BinFullness getFullnessByTrashLog(TrashLog trashLog) {
        StatusReport fullnessReport = queryOne("SELECT * FROM status_reports WHERE bin_id = ? AND issue = ? AND created_at = ? And description = ?",
                trashLog.binID(), ReportType._FULLNESS_UPDATE, trashLog.createdAt(), trashLog.trashLogId());
        if (fullnessReport == null) {
            return BinFullness.UNKNOWN;
        }
        return fullnessReport.getFullness();
    }

    /**
     * Adds a StatusReport to the database
     * @param statusReportToAdd StatusReport to add
     * @return Report id in the database, or -1 on SQL error
     */
    @Override
    public int add(StatusReport statusReportToAdd) {
        String sql = "INSERT INTO status_reports (bin_id, user_id, fullness, issue, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        return executeInsert(sql,
                statusReportToAdd.getBinId(),
                statusReportToAdd.getUserId(),
                statusReportToAdd.getFullness(),
                statusReportToAdd.getIssue(),
                statusReportToAdd.getDescription(),
                statusReportToAdd.getSubmittedAt());
    }

    /**
     * Deletes a StatusReport from the database
     * @param id id of StatusReport to delete
     */
    @Override
    public void delete(int id) {
        deleteById("status_reports", id);
    }

    /**
     * Updates the StatusReport in the database to the new StatusReport if they have the same id
     * @param statusReportToUpdate StatusReport that needs to be updated
     */
    @Override
    public void update(StatusReport statusReportToUpdate) {
        String sql = "UPDATE status_reports SET bin_id = ?, user_id = ?, fullness = ?, issue = ?, "
                + "description = ?, created_at = ? WHERE id = ?";
        int rows = executeUpdate(sql,
                statusReportToUpdate.getBinId(),
                statusReportToUpdate.getUserId(),
                statusReportToUpdate.getFullness(),
                statusReportToUpdate.getIssue(),
                statusReportToUpdate.getDescription(),
                statusReportToUpdate.getSubmittedAt(),
                statusReportToUpdate.getId());
        if (rows == 0) {
            log.warn("Update affected 0 rows for status report id {}", statusReportToUpdate.getId());
        }
    }
}