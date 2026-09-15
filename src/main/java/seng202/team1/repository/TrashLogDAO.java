package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Database Access Object that handles all trash related actions to the database
 *
 * @author Marcus Demafeliz
 */
public class TrashLogDAO implements DAOInterface<TrashLog> {
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(TrashLogDAO.class);

    /**
     * Creates a new TrashLogDAO object and gets a reference to the database singleton
     */
    public TrashLogDAO(){databaseManager = DatabaseManager.getInstance(); }


    /**
     * Converts string from database to TrashType
     * @param type string name
     * @return trash type that matches the string
     */
    private TrashType convertStringToType(String type) {
        return switch(type.toUpperCase(Locale.ROOT)){
            case "PLASTIC_BOTTLE" -> TrashType.PLASTIC_BOTTLE;
            case "PAPER" -> TrashType.PAPER;
            case "CARDBOARD_BOX" -> TrashType.CARDBOARD_BOX;
            case "COFFEE_CUP" -> TrashType.COFFEE_CUP;
            default -> TrashType.OTHER;
        };
    }

    /**
     * Convert the integer from the result set into an Instant to store in TrashLog
     * @param rs result set
     * @param column column name
     * @return the Instant result
     * @throws SQLException
     */
    private Instant getInstant(ResultSet rs, String column) throws SQLException {
        long millis = rs.getLong(column);
        return rs.wasNull() ? null : Instant.ofEpochMilli(millis);
    }

    /**
     * Convert integer to Instant
     * @param ps prepared statement
     * @param index for the SQL query
     * @param instant result
     * @throws SQLException
     */
    private void setInstant(PreparedStatement ps, int index, Instant instant) throws SQLException {
        if (instant == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, instant.toEpochMilli());
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
        String sql = "SELECT * FROM trash_log";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trash.add(new TrashLog(
                        rs.getInt("id"),
                        getInstant(rs, "created_at"),
                        getInstant(rs, "disposed_at"),
                        rs.getInt("user_id"),
                        rs.getInt("bin_id"),
                        convertStringToType(rs.getString("type")),
                        rs.getInt("weight")));
            }
            return trash;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets individual trash log from database by id
     * @param id id of object to get
     * @return TrashLog trash log from database that matches id
     */
    @Override
    public TrashLog getOne(int id) {
        TrashLog trashLog = null;
        String sql = "SELECT * FROM trash_log WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trashLog = new TrashLog(
                            rs.getInt("id"),
                            getInstant(rs, "created_at"),
                            getInstant(rs, "disposed_at"),
                            rs.getInt("user_id"),
                            rs.getInt("bin_id"),
                            convertStringToType(rs.getString("type")),
                            rs.getInt("weight"));
                }
                return trashLog;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Adds an individual trashLog to database
     * @param trashLogToAdd object of type T to add
     * @return true if no error, false if sql error
     */
    @Override
    public int add(TrashLog trashLogToAdd) {
        String sql = "INSERT INTO trash_log (created_at, disposed_at, user_id, bin_id, type, weight) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInstant(ps, 1, trashLogToAdd.createdAt());
            setInstant(ps, 2, trashLogToAdd.disposedAt());
            ps.setInt(3, trashLogToAdd.userID());
            ps.setInt(4, trashLogToAdd.binID());
            ps.setString(5, trashLogToAdd.trashType().toString());
            ps.setInt(6, trashLogToAdd.trashWeight());
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
     * @param id id of object to delete
     */
    @Override
    public void delete (int id) {
        String sql = "DELETE FROM trash_log WHERE id = ?";
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
        String sql = "UPDATE trash_log SET disposed_at = ?, user_id = ?, bin_id = ?, type = ?, weight = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setInstant(ps, 1, trashLogToUpdate.disposedAt());
            ps.setInt(2, trashLogToUpdate.userID());
            ps.setInt(3, trashLogToUpdate.binID());
            ps.setString(4, trashLogToUpdate.trashType().toString());
            ps.setInt(5, trashLogToUpdate.trashWeight());
            ps.setInt(6, trashLogToUpdate.trashLogId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for trash id {}", trashLogToUpdate.trashLogId());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

}

