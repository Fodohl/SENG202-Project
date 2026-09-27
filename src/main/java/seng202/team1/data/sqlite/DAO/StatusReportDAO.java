package seng202.team1.data.sqlite.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.User;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.sqlite.SqlTimeUtils;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.trashlog.TrashLog;

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
            case "BLOCKED" -> ReportType.BLOCKED;
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
                        rs.getInt("bin_id"),
                        new User(rs.getInt("user_id"),
                                rs.getString("user")),
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
        String sql = "SELECT * FROM status_report WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report = new StatusReport(
                            rs.getInt("bin_id"),
                            new User(rs.getInt("user_id"),
                                    rs.getString("user")),
                            convertStringToBinFullness(rs.getString("fullness")),
                            convertStringToIssue(rs.getString("issue")),
                            rs.getString("description"),
                            getInstant(rs, "created_at")));
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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reports.add(new StatusReport(
                        rs.getInt("bin_id"),
                        new User(rs.getInt("user_id"),
                                rs.getString("user")),
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

    public List<StatusReport> getReportsByUserId(int UserId) {
        List<StatusReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM status_reports WHERE user_id = ?";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reports.add(new StatusReport(
                        rs.getInt("bin_id"),
                        new User(rs.getInt("user_id"),
                                rs.getString("user")),
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

    public int add(StatusReport statusReportToAdd) {
        String sql = "INSERT INTO status_report (bin_id, user, user_id, fullness, issue, description, created_at) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,  statusReportToAdd.getBinId());
            ps.setString(2, statusReportToAdd.getSubmittedBy().getName());
            ps.setInt(3, statusReportToAdd.getSubmittedBy().getUserID());
            ps.setString(4, statusReportToAdd.getFullness().toString());
            ps.setString(5, statusReportToAdd.getIssue().toString());
            ps.setString(6, statusReportToAdd.getDescription());
            setInstant(ps, 7, statusReportToAdd.getSubmittedAt());
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
}
