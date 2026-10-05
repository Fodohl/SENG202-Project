package seng202.team1.database.DAO;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import seng202.team1.database.DatabaseManager;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Handles all bin related actions to the database
 * @author Marcus Demafeliz
 */
public class BinDAO extends BaseDAO<Bin> {

    /**
     * Instantiates BinDAO and gets a reference to the database singleton
     * @param databaseManager database singleton
     */
    public BinDAO(DatabaseManager databaseManager) {
        super(databaseManager);
    }


    @Override
    protected Bin fromRow(ResultSet rs) throws SQLException {
        return new Bin(
                rs.getInt("id"),
                rs.getDouble("lat"),
                rs.getDouble("lng"),
                convertStringToCategory(rs.getString("waste_type")),
                getInstant(rs, "updated_at"),
                rs.getString("photo_url"),
                rs.getString("area"),
                convertStringToFullness(rs.getString("fullness")));
    }

    /**
     * Gets all bins in the database
     * @return all bins as a list
     */
    @Override
    public List<Bin> getAll() {
        return queryList("SELECT * FROM bins");
    }

    /**
     * Gets individual bin from database from id
     * @param id id of object to get
     * @return bin matching the id, or null if none
     */
    @Override
    public Bin getOne(int id) {
        return queryOne("SELECT * FROM bins WHERE id = ?", id);
    }

    /**
     * Gets all bins updated after the given time
     * @param instant cutoff time, or null to get every bin
     * @return bins with an update time after the cutoff
     */
    public List<Bin> getBinsAfter(Instant instant) {
        // No known update time
        if (instant == null) {
            return getAll();
        }
        return queryList("SELECT * FROM bins WHERE updated_at > ?", instant);
    }

    /**
     * Add individual bin to the database
     * @param toAdd bin to add
     * @return returns id of new bin if no error, -1 if SQL error
     */
    @Override
    public int add(Bin toAdd) {
        String sql = "INSERT INTO bins (id, lat, lng, waste_type, updated_at, photo_url, area) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeInsert(sql,
                toAdd.getID(),
                toAdd.getLatitude(),
                toAdd.getLongitude(),
                toAdd.getCategory(),
                toAdd.getUpdated(),
                toAdd.getPhotoURL(),
                toAdd.getAreaName());
    }

    /**
     * Adds a batch of bins to the database, ignoring bins whose id already exists
     * @param bins list of bins to add
     */
    public void addBatch(List<Bin> bins) {
        try (Connection conn = databaseManager.connect()) {
            DSLContext context = databaseManager.getDSLContext(conn);
            context.transaction(configuration -> {
                DSLContext ctx = DSL.using(configuration);
                for (Bin bin : bins) {
                    Long updatedMillis = bin.getUpdated() != null
                            ? bin.getUpdated().toEpochMilli()
                            : null;
                    ctx.insertInto(DSL.table("bins"))
                            .columns(
                                    DSL.field("id"),
                                    DSL.field("lat"),
                                    DSL.field("lng"),
                                    DSL.field("waste_type"),
                                    DSL.field("updated_at"),
                                    DSL.field("photo_url"),
                                    DSL.field("area"))
                            .values(
                                    bin.getID(),
                                    bin.getLatitude(),
                                    bin.getLongitude(),
                                    bin.getCategory().toString(),
                                    updatedMillis,
                                    bin.getPhotoURL(),
                                    bin.getAreaName())
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
     * @param id id of bin to delete
     */
    @Override
    public void delete(int id) {
        deleteById("bins", id);
    }

    /**
     * Updates bin from database by id
     * @param toUpdate Bin with the updated parameters, matching the id of the bin to update
     */
    @Override
    public void update(Bin toUpdate) {
        String sql = "UPDATE bins SET lat = ?, lng = ?, waste_type = ?, updated_at = ?, photo_url = ?, "
                + "area = ?, fullness = ? WHERE id = ?";
        int rows = executeUpdate(sql,
                toUpdate.getLatitude(),
                toUpdate.getLongitude(),
                toUpdate.getCategory(),
                toUpdate.getUpdated(),
                toUpdate.getPhotoURL(),
                toUpdate.getAreaName(),
                toUpdate.getFullness(),
                toUpdate.getID());
        if (rows == 0) {
            log.warn("Update affected 0 rows for bin id {}", toUpdate.getID());
        }
    }

    /**
     * Updates the fullness of several bins in a single transaction
     * @param fullness map of bin id to new fullness
     */
    public void updateFullness(HashMap<Integer, BinFullness> fullness) {
        // Fullness hasn't changed since last check
        if (fullness == null || fullness.isEmpty()) {
            return;
        }

        long nowMillis = Instant.now().toEpochMilli();

        try (Connection conn = databaseManager.connect()) {
            DSLContext context = databaseManager.getDSLContext(conn);
            context.transaction(configuration -> {
                DSLContext ctx = DSL.using(configuration);
                for (Map.Entry<Integer, BinFullness> entry : fullness.entrySet()) {
                    ctx.update(DSL.table("bins"))
                            .set(DSL.field("fullness"), entry.getValue().toString())
                            .set(DSL.field("updated_at"), nowMillis)
                            .where(DSL.field("id").eq(entry.getKey()))
                            .execute();
                }
            });
        } catch (Exception e) {
            log.error(e);
        }
    }
}