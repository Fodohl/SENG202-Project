package seng202.team1.data.sqlite.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.User;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.sqlite.SqlTimeUtils;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatusReportDAO extends SqlTimeUtils implements DAOInterface<StatusReport> {
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(StatusReportDAO.class);

    /**
     * Instantiates StatusReportDAO and gets a reference to the database singleton
     * @param databaseManager database singleton
     */
    public StatusReportDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

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

    public List<StatusReport> getAllFullnessReports(int binId) {
        List<StatusReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM status_reports WHERE issue = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "_FULLNESS_UPDATE");
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
