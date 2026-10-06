package seng202.team1.database.DAO;

import seng202.team1.database.DatabaseManager;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;

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
public class TrashLogDAO extends BaseDAO<TrashLog> {

    /**
     * Creates a new TrashLogDAO object and gets a reference to the database singleton
     * @param databaseManager database singleton
     */
    public TrashLogDAO(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    protected TrashLog fromRow(ResultSet rs) throws SQLException {
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

    /**
     * Gets all trashLogs in database
     *
     * @return a list of all trash logs
     */
    @Override
    public List<TrashLog> getAll() {
        return queryList("SELECT * FROM trash_logs");
    }

    /**
     * Gets individual trash log from database by id
     *
     * @param id id of object to get
     * @return TrashLog that matches id, or null if none
     */
    @Override
    public TrashLog getOne(int id) {
        return queryOne("SELECT * FROM trash_logs WHERE id = ?", id);
    }

    /**
     * Gets all trash logs that have not been synced yet
     *
     * @return list of trash logs with no sync time
     */
    public List<TrashLog> getUnsynced() {
        return queryList("SELECT * FROM trash_logs WHERE synced_at IS NULL");
    }

    /**
     * Adds an individual trashLog to database
     *
     * @param trashLogToAdd trash log to add
     * @return id of new trash log if no error, -1 if SQL error
     */
    @Override
    public int add(TrashLog trashLogToAdd) {
        String sql = "INSERT INTO trash_logs (created_at, disposed_at, synced_at, user_id, bin_id, type, weight) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeInsert(sql,
                trashLogToAdd.createdAt(),
                trashLogToAdd.disposedAt(),
                trashLogToAdd.syncedAt(),
                trashLogToAdd.userID(),
                trashLogToAdd.binID(),
                trashLogToAdd.trashType(),
                trashLogToAdd.trashWeight());
    }

    /**
     * Delete trashLog from database by id
     *
     * @param id id of object to delete
     */
    @Override
    public void delete(int id) {
        deleteById("trash_logs", id);
    }

    /**
     * Updates a trash log in the database (matched by id)
     *
     * @param trashLogToUpdate trash log with the new values
     */
    @Override
    public void update(TrashLog trashLogToUpdate) {
        String sql = "UPDATE trash_logs SET disposed_at = ?, synced_at = ?, user_id = ?, bin_id = ?, "
                + "type = ?, weight = ? WHERE id = ?";
        int rows = executeUpdate(sql,
                trashLogToUpdate.disposedAt(),
                trashLogToUpdate.syncedAt(),
                trashLogToUpdate.userID(),
                trashLogToUpdate.binID(),
                trashLogToUpdate.trashType(),
                trashLogToUpdate.trashWeight(),
                trashLogToUpdate.trashLogId());
        if (rows == 0) {
            log.warn("Update affected 0 rows for trash id {}", trashLogToUpdate.trashLogId());
        }
    }


    /**
     * Counts trash logs since the given instant
     *
     * @param since start of region
     * @return number of logs, -1 on SQL error
     */
    public int countSince(Instant since) {
        return queryInt("SELECT COUNT(*) FROM trash_logs WHERE disposed_at >= ?", since);
    }

     /**
     * Gets all trash logs from database by user id
     *
     * @param id user id of object to get
     * @return TrashLog trash log from database that matches id, or null if none
     */
    public List<TrashLog> getByUser(int id) {
        return queryList("SELECT * FROM trash_logs WHERE user_id = ?", id);
    }
}

