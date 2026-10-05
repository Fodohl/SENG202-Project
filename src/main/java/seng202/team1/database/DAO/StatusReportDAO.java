package seng202.team1.database.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.SqlTimeUtils;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Database Access Object that handles all {@link StatusReport} related actions to the database
 *
 * @author Archie Georgeson
 */
public class StatusReportDAO extends SqlTimeUtils implements DAOInterface<StatusReport> {

    /// DatabaseManager managing all the DAO objects
    private final DatabaseManager databaseManager;
    /// Logger for recording StatusReportDAO database operations and errors
    private static final Logger log = LogManager.getLogger(StatusReportDAO.class);

    /**
     * Instantiates StatusReportDAO and gets a reference to the database singleton
     * @param databaseManager database singleton
     */
    public StatusReportDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Converts the string representation saved in the database to a {@link ReportType}
     * @param issueString String representation of report type
     * @return ReportType associated with the report
     */
    private ReportType convertStringToIssue(String issueString) {
        return switch (issueString.toUpperCase(Locale.ROOT)) {
            case "_FULLNESS_UPDATE" -> ReportType._FULLNESS_UPDATE;
            case "GRAFFITI" -> ReportType.GRAFFITI;
            case "DAMAGED" -> ReportType.DAMAGED;
            case "MISSING" -> ReportType.MISSING;
            case "FIRE_HAZARD" -> ReportType.FIRE_HAZARD;
            case "ILLEGAL_DUMPING" -> ReportType.ILLEGAL_DUMPING;
            case "BLOCKED_ACCESS" -> ReportType.BLOCKED_ACCESS;
            case "OTHER" -> ReportType.OTHER;
            default -> throw new IllegalStateException("Unexpected value: " + issueString.toUpperCase(Locale.ROOT));
        };
    }

    /**
     * Converts the string representation saved in the database to a {@link BinFullness}
     * @param binFullnessString String representation of report type
     * @return BinFullness associated with the report
     */
    private BinFullness convertStringToBinFullness(String binFullnessString) {
        return switch (binFullnessString.toUpperCase(Locale.ROOT)) {
            case "UNKNOWN" -> BinFullness.UNKNOWN;
            case "FULL" -> BinFullness.FULL;
            case "SEMI_FULL" -> BinFullness.SEMI_FULL;
            case "EMPTY" -> BinFullness.EMPTY;
            case "OVERFLOWING" -> BinFullness.OVERFLOWING;
            default -> throw new IllegalStateException("Unexpected value: " + binFullnessString.toUpperCase(Locale.ROOT));
        };
    }

    /**
     * Gets all status reports from the database
     * @return a list of all status reports
     */
    @Override
    public List<StatusReport> getAll() {
        List<StatusReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM status_reports";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reports.add(new StatusReport(
                        rs.getInt("id"),
                        rs.getInt("bin_id"),
                        rs.getInt("user_id"),
                        convertStringToBinFullness(rs.getString("fullness")),
                        convertStringToIssue(rs.getString("issue")),
                        rs.getString("description"),
                        getInstant(rs, "created_at")));
            }
            return reports;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets a status report related to a certain report id
     * @param id id of object to get
     * @return Status report related to id
     */
    @Override
    public StatusReport getOne(int id) {
        StatusReport report = null;
        String sql = "SELECT * FROM status_reports WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report = new StatusReport(
                            rs.getInt("id"),
                            rs.getInt("bin_id"),
                            rs.getInt("user_id"),
                            convertStringToBinFullness(rs.getString("fullness")),
                            convertStringToIssue(rs.getString("issue")),
                            rs.getString("description"),
                            getInstant(rs, "created_at"));
                }
                return report;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Gets all reports relating to a bin id
     * @param binId id of the desired bin
     * @return a list of StatusReports relating to the supplied bin id
     */
    public List<StatusReport> getReportsByBinId(int binId) {
        List<StatusReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM status_reports WHERE bin_id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, binId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reports.add(new StatusReport(
                        rs.getInt("id"),
                        rs.getInt("bin_id"),
                        rs.getInt("user_id"),
                        convertStringToBinFullness(rs.getString("fullness")),
                        convertStringToIssue(rs.getString("issue")),
                        rs.getString("description"),
                        getInstant(rs, "created_at")));
            }
            return reports;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets all reports relating to a user
     * @param userId id of the desired user
     * @return a list of StatusReports relating to the supplied user id
     */
    public List<StatusReport> getReportsByUserId(int userId) {
        List<StatusReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM status_reports WHERE user_id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reports.add(new StatusReport(
                        rs.getInt("id"),
                        rs.getInt("bin_id"),
                        rs.getInt("user_id"),
                        convertStringToBinFullness(rs.getString("fullness")),
                        convertStringToIssue(rs.getString("issue")),
                        rs.getString("description"),
                        getInstant(rs, "created_at")));
            }
            return reports;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets all fullness reports relating to a certain bin
     * @param binId id of the desired bin
     * @return a list of fullness StatusReports relating to the supplied bin
     */
    public List<StatusReport> getFullnessReportsByBinId(int binId) {
        List<StatusReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM status_reports WHERE bin_id = ? AND issue = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, binId);
            ps.setString(2, "_FULLNESS_UPDATE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reports.add(new StatusReport(
                        rs.getInt("id"),
                        rs.getInt("bin_id"),
                        rs.getInt("user_id"),
                        convertStringToBinFullness(rs.getString("fullness")),
                        convertStringToIssue(rs.getString("issue")),
                        rs.getString("description"),
                        getInstant(rs, "created_at")));
            }
            return reports;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a StatusReport to the database
     * @param statusReportToAdd StatusReport to add
     * @return Report id in the database
     */
    @Override
    public int add(StatusReport statusReportToAdd) {
        String sql = "INSERT INTO status_reports (bin_id, user_id, fullness, issue, description, created_at) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,  statusReportToAdd.getBinId());
            ps.setInt(2, statusReportToAdd.getUserId());
            ps.setString(3, statusReportToAdd.getFullness().toString());
            ps.setString(4, statusReportToAdd.getIssue().toString());
            ps.setString(5, statusReportToAdd.getDescription());
            setInstant(ps, 6, statusReportToAdd.getSubmittedAt());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int insertId = -1;
            if (rs.next()) {
                insertId = rs.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Deletes a StatusReport from the database
     * @param id id of StatusReport to delete
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM status_reports WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates the StatusReport in the database to the new StatusReport if they have the same id
     * @param statusReportToUpdate StatusReport that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public void update(StatusReport statusReportToUpdate) {
        String sql = "UPDATE status_reports SET bin_id = ?, user_id = ?, fullness = ?, issue = ?, description = ?, created_at = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,  statusReportToUpdate.getBinId());
            ps.setInt(2, statusReportToUpdate.getUserId());
            ps.setString(3, statusReportToUpdate.getFullness().toString());
            ps.setString(4, statusReportToUpdate.getIssue().toString());
            ps.setString(5, statusReportToUpdate.getDescription());
            setInstant(ps, 6, statusReportToUpdate.getSubmittedAt());
            ps.setInt(7, statusReportToUpdate.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for status report id {}", statusReportToUpdate.getId());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }
}
