package seng202.team1.database.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.SqlTimeUtils;
import seng202.team1.types.trashdb.LeaderBoardEntry;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static seng202.team1.helpers.DateHelpers.toOffsetTime;

public class LeaderboardDAO extends SqlTimeUtils implements DAOInterface<LeaderBoardEntry> {

    /// DatabaseManager managing all the DAO objects
    private final DatabaseManager databaseManager;

    /// Logger for recording LeaderboardDAO database operations and errors
    private static final Logger log = LogManager.getLogger(LeaderboardDAO.class);


    public LeaderboardDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

    }

    private LeaderBoardEntry rowToEntry(ResultSet rs) throws SQLException {
        return new LeaderBoardEntry(
                rs.getInt("place"),
                rs.getInt("user_id"),
                rs.getString("user_name"),
                rs.getInt("log_count"),
                rs.getDouble("tree_days"),
                getInstant(rs, "updated_at")
        );
    }

    /**
     * Gets all leaderboard entries in the database
     *
     * @return all entries as a list
     */
    @Override
    public List<LeaderBoardEntry> getAll() {

        List<LeaderBoardEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM leaderboard ORDER BY place";

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next())
                entries.add(rowToEntry(rs));

            return entries;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets individual leaderboard entry from database from id
     *
     * @param place position of entry to get
     * @return entry matching the id
     */
    @Override
    public LeaderBoardEntry getOne(int place) {
        LeaderBoardEntry entry = null;
        String sql = "SELECT * FROM leaderboard WHERE place = ?";

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, place);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entry = rowToEntry(rs);
                }
                return entry;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Add individual entry to the database
     *
     * @param toAdd leaderboard entry to add to add
     * @return returns id of new entry if no error, -1 if SQL error
     */
    @Override
    public int add(LeaderBoardEntry toAdd) {
        String sql = "INSERT INTO leaderboard (place, user_id, user_name, log_count, tree_days, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, toAdd.place());
            ps.setInt(2, toAdd.userId());
            ps.setString(3, toAdd.userName());
            ps.setInt(4, toAdd.logCount());
            ps.setDouble(5, toAdd.treeDays());
            ps.setLong(6, Instant.now().toEpochMilli());

            ps.executeUpdate();
            return toAdd.place();
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Adds a batch of leaderboard entries to the database
     *
     * @param entries list of leaderboard entries to add
     */
    public void addBatch(List<LeaderBoardEntry> entries) {
        String sql = "INSERT INTO leaderboard (place, user_id, user_name, log_count, tree_days, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            DSLContext context = databaseManager.getDSLContext(conn);
            context.transaction(configuration -> {
                for (LeaderBoardEntry entry : entries) {
                    ps.setInt(1, entry.place());
                    ps.setInt(2, entry.userId());
                    ps.setString(3, entry.userName());
                    ps.setInt(4, entry.logCount());
                    ps.setDouble(5, entry.treeDays());
                    ps.setLong(6, Instant.now().toEpochMilli());
                    ps.addBatch();
                }
                ps.executeBatch();
            });
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Deletes entry from database by place
     *
     * @param id place of entry to delete
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM leaderboard WHERE place = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates entry from database by place
     *
     * @param toUpdate Entry with the updated parameter matching place with the entry that needs to be updated
     */
    @Override
    public void update(LeaderBoardEntry toUpdate) {
        String sql = "UPDATE leaderboard SET user_id = ?, user_name = ?, log_count = ?, tree_days = ?, updated_at = ? WHERE place = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, toUpdate.userId());
            ps.setString(2, toUpdate.userName());
            ps.setInt(3, toUpdate.logCount());
            ps.setDouble(4, toUpdate.treeDays());
            ps.setLong(5, Instant.now().toEpochMilli());
            ps.setInt(6, toUpdate.place());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for community leaderboard place {}", toUpdate.place());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    public void clear() {
        String sql = "DELETE FROM leaderboard";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }
}
