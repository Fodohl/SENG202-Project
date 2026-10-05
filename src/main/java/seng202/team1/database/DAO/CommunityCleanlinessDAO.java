package seng202.team1.database.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.trashdb.CommunityCleanliness;
import seng202.team1.database.SqlTimeUtils;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static seng202.team1.helpers.DateHelpers.toOffsetTime;

public class CommunityCleanlinessDAO extends SqlTimeUtils implements DAOInterface<CommunityCleanliness> {

    /// DatabaseManager managing all the DAO objects
    private final DatabaseManager databaseManager;

    /// Logger for recording CommunityCleanlinessDAO database operations and errors
    private static final Logger log = LogManager.getLogger(CommunityCleanlinessDAO.class);


    public CommunityCleanlinessDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

    }


    private CommunityCleanliness rowToEntry(ResultSet rs) throws SQLException {
        return new CommunityCleanliness(
                rs.getInt("id"),
                rs.getInt("year"),
                rs.getInt("month"),
                rs.getInt("log_count"),
                getInstant(rs, "updated_at")
        );
    }

    /**
     * Gets all cleanliness entries in the database
     *
     * @return all bins as a list
     */
    @Override
    public List<CommunityCleanliness> getAll() {

        List<CommunityCleanliness> counts = new ArrayList<>();
        String sql = "SELECT * FROM community_cleanliness";

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {


            while (rs.next())
                counts.add(rowToEntry(rs));

            return counts;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets individual cleanliness entry from database from id
     *
     * @param id id of object to get
     * @return entry matching the id
     */
    @Override
    public CommunityCleanliness getOne(int id) {
        CommunityCleanliness count = null;
        String sql = "SELECT * FROM community_cleanliness WHERE id = ?";

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count = rowToEntry(rs);
                }
                return count;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Add individual cleanliness entry to the database
     *
     * @param toAdd cleanliness entry to add to add
     * @return returns id of new bin if no error, -1 if SQL error
     */
    @Override
    public int add(CommunityCleanliness toAdd) {
        String sql = "INSERT INTO community_cleanliness (year, month, log_count, updated_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, toAdd.year());
            ps.setInt(2, toAdd.month());
            ps.setDouble(3, toAdd.log_count());
            ps.setLong(4, Instant.now().toEpochMilli());

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
     * Adds a batch of cleanliness entries to the database
     *
     * @param entries list of cleanliness entries to add
     */
    public void addBatch(List<CommunityCleanliness> entries) {
        try (Connection conn = databaseManager.connect()) {
            DSLContext context = databaseManager.getDSLContext(conn);
            context.transaction(configuration -> {
                DSLContext ctx = DSL.using(configuration);
                for (CommunityCleanliness entry : entries) {
                    ctx.insertInto(DSL.table("community_cleanliness"))
                            .columns(
                                    DSL.field("id"),
                                    DSL.field("year"),
                                    DSL.field("month"),
                                    DSL.field("log_count"),
                                    DSL.field("updated_at")
                            )
                            .values(
                                    entry.id(),
                                    entry.year(),
                                    entry.month(),
                                    entry.log_count(),
                                    Instant.now().toEpochMilli()
                            )
                            .onDuplicateKeyIgnore()
                            .execute();
                }
            });
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Deletes bin from database by id
     *
     * @param id id of bin to delete
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM community_cleanliness WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates bin from database by id
     *
     * @param toUpdate Bin with the updated parameter matching id with the bin that needs to be updated
     */
    @Override
    public void update(CommunityCleanliness toUpdate) {
        String sql = "UPDATE community_cleanliness SET year = ?, month = ?, log_count = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, toUpdate.year());
            ps.setInt(2, toUpdate.month());
            ps.setInt(3, toUpdate.log_count());
            ps.setLong(4, Instant.now().toEpochMilli());
            ps.setInt(5, toUpdate.id());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for community cleanliness id {}", toUpdate.id());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Gets the entry for a specific month
     *
     * @return the entry or null if no data for that month
     */
    public CommunityCleanliness getByMonth(int year, int month) {
        String sql = "SELECT * FROM community_cleanliness WHERE year = ? AND month = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rowToEntry(rs) : null;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Gets this month's entry
     *
     * @return The entry or null if nothing has been logged yet
     */
    public CommunityCleanliness getThisMonth() {

        // Try get the current month
        OffsetDateTime now = toOffsetTime(Instant.now());
        CommunityCleanliness current = getByMonth(now.getYear(), now.getMonthValue());

        if(current != null)
            return  current;

        // There must not be a entry for this month, so create it
        add(new CommunityCleanliness(-1, now.getYear(), now.getMonthValue(), 0, null));
        return getByMonth(now.getYear(), now.getMonthValue());

    }

    /**
     * Gets the current month and the 5 months before it
     */
    public List<CommunityCleanliness> getPastSixMonths() {
        OffsetDateTime now = toOffsetTime(Instant.now());
        int current = now.getYear() * 12 + now.getMonthValue();
        int start = current - 5;

        List<CommunityCleanliness> counts = new ArrayList<>();
        String sql = "SELECT * FROM community_cleanliness "
                + "WHERE year * 12 + month BETWEEN ? AND ? ORDER BY year, month";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, current);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    counts.add(rowToEntry(rs));
            }
            return counts;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }
}
