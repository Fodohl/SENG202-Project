package seng202.team1.database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.SqlTimeUtils;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;

/**
 * Database Access Object that handles all trash related actions to the database
 *
 * @author Marcus Demafeliz
 */
public class TrashLogDAO extends SqlTimeUtils implements DAOInterface<TrashLog> {
    /// DatabaseManager managing all the DAO objects
    private final DatabaseManager databaseManager;
    /// Logger for recording StatusReportDAO database operations and errors
    private static final Logger log = LogManager.getLogger(TrashLogDAO.class);

    /**
     * Creates a new TrashLogDAO object and gets a reference to the database singleton
     */
    public TrashLogDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }


    /**
     * Converts string from database to TrashType
     *
     * @param type string name
     * @return trash type that matches the string
     */
    private TrashType convertStringToType(String type) {
        if (type == null) {
            return TrashType.OTHER;
        }
        try {
            return TrashType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return TrashType.OTHER;
        }
    }

    /**
     * Gets all trashLogs in database
     *
     * @return a list of all trash logs
     */
    @Override
    public List<TrashLog> getAll() {
        List<TrashLog> trash = new ArrayList<>();
        String sql = "SELECT * FROM trash_logs";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Add all rows
            while (rs.next())
                trash.add(toTrashLog(rs));

            return trash;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets individual trash log from database by id
     *
     * @param id id of object to get
     * @return TrashLog trash log from database that matches id
     */
    @Override
    public TrashLog getOne(int id) {
        TrashLog trashLog = null;
        String sql = "SELECT * FROM trash_logs WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                    trashLog = toTrashLog(rs);
                return trashLog;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Adds an individual trashLog to database
     *
     * @param trashLogToAdd object of type T to add
     * @return id of new trash log if no error, -1 if SQL error
     */
    @Override
    public int add(TrashLog trashLogToAdd) {
        String sql = "INSERT INTO trash_logs (created_at, disposed_at, synced_at, user_id, bin_id, type, weight) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInstant(ps, 1, trashLogToAdd.createdAt());
            setInstant(ps, 2, trashLogToAdd.disposedAt());
            setInstant(ps, 3, trashLogToAdd.syncedAt());
            ps.setInt(4, trashLogToAdd.userID());
            ps.setInt(5, trashLogToAdd.binID());
            ps.setString(6, trashLogToAdd.trashType().toString());
            ps.setDouble(7, trashLogToAdd.trashWeight());
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
     * Delete trashLog from database by id
     *
     * @param id id of object to delete
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM trash_logs WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    @Override
    public void update(TrashLog trashLogToUpdate) {
        String sql = "UPDATE trash_logs SET disposed_at = ?, synced_at = ?, user_id = ?, bin_id = ?, type = ?, weight = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setInstant(ps, 1, trashLogToUpdate.disposedAt());
            setInstant(ps, 2, trashLogToUpdate.syncedAt());
            ps.setInt(3, trashLogToUpdate.userID());
            ps.setInt(4, trashLogToUpdate.binID());
            ps.setString(5, trashLogToUpdate.trashType().toString());
            ps.setDouble(6, trashLogToUpdate.trashWeight());
            ps.setInt(7, trashLogToUpdate.trashLogId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for trash id {}", trashLogToUpdate.trashLogId());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    private TrashLog toTrashLog(ResultSet rs) throws SQLException {
        return new TrashLog(
                rs.getInt("id"),
                getInstant(rs, "created_at"),
                getInstant(rs, "disposed_at"),
                getInstant(rs, "synced_at"),
                rs.getInt("user_id"),
                rs.getInt("bin_id"),
                convertStringToType(rs.getString("type")),
                rs.getDouble("weight"));
    }

    public List<TrashLog> getUnsynced() {


        List<TrashLog> trash = new ArrayList<>();

        // Get all without a sync state
        String sql = "SELECT * FROM trash_logs WHERE synced_at IS NULL";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Convert all rows into trashlog objs
            while (rs.next())
                trash.add(toTrashLog(rs));

            return trash;

        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Counts trash logs since the given instant
     *
     * @param since start of region
     * @return number of logs, -1 on SQL error
     */
    public int countSince(Instant since) {
        String sql = "SELECT COUNT(*) FROM trash_logs WHERE disposed_at >= ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setInstant(ps, 1, since);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }
}

