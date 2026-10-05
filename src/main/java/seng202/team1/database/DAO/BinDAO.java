package seng202.team1.database.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.SqlTimeUtils;


import java.sql.*;
import java.time.Instant;
import java.util.*;

/**
 * Handles all bin related actions to the database
 * @author Marcus Demafeliz
 */
public class BinDAO extends SqlTimeUtils implements DAOInterface<Bin> {
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(BinDAO.class);


    public BinDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

    }

    /**
     * Converts string from database to BinCategory
     * @param category string name
     * @return bin category that matches the string
     */
    private BinCategory convertStringToCategory(String category) {
        return switch(category.toUpperCase(Locale.ROOT)) {
            case "RECYCLABLE" -> BinCategory.RECYCLABLE;
            case "ORGANIC" -> BinCategory.ORGANIC;
            case "E-WASTE", "E_WASTE" -> BinCategory.E_WASTE;
            default ->  BinCategory.GENERAL_WASTE;
        };
    }


    /**
     * Gets all bins in the database
     * @return all bins as a list
     */
    @Override
    public List<Bin> getAll() {
        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT * FROM bins";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bins.add(new Bin(
                        rs.getInt("id"),
                        rs.getDouble("lat"),
                        rs.getDouble("lng"),
                        convertStringToCategory(rs.getString("waste_type")),
                        getInstant(rs, "updated_at"),
                        rs.getString("photo_url"),
                        rs.getString("area"),
                        convertStringToFullness(rs.getString("fullness"))
                ));
            }
            return bins;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets individual bin from database from id
     * @param id id of object to get
     * @return bin matching the id
     */
    @Override
    public Bin getOne(int id) {
        Bin bin = null;
        String sql = "SELECT * FROM bins WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs  = ps.executeQuery()) {
                while (rs.next()) {
                    bin = new Bin(
                            rs.getInt("id"),
                            rs.getDouble("lat"),
                            rs.getDouble("lng"),
                            convertStringToCategory(rs.getString("waste_type")),
                            getInstant(rs, "updated_at"),
                            rs.getString("photo_url"),
                            rs.getString("area"),
                            convertStringToFullness(rs.getString("fullness"))
                            );
                }
                return bin;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Add individual bin to the database
     * @param toAdd bin to add
     * @return returns id of new bin if no error, -1 if SQL error
     */
    @Override
    public int add(Bin toAdd) {
        String sql = "INSERT INTO bins (id, lat, lng, waste_type, updated_at, photo_url, area) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, toAdd.getID());
            ps.setDouble(2, toAdd.getLatitude());
            ps.setDouble(3, toAdd.getLongitude());
            ps.setString(4, toAdd.getCategory().toString());
            setInstant(ps, 5, toAdd.getUpdated());
            ps.setString(6, toAdd.getPhotoURL());
            ps.setString(7, toAdd.getAreaName());
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
     * Adds a batch of bins to the database
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
                                    DSL.field("area")
                            )
                            .values(
                                    bin.getID(),
                                    bin.getLatitude(),
                                    bin.getLongitude(),
                                    bin.getCategory().toString(),
                                    updatedMillis,
                                    bin.getPhotoURL(),
                                    bin.getAreaName()
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
     * @param id id of bin to delete
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM bins WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps =conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates bin from database by id
     * @param toUpdate Bin with the updated parameter matching id with the bin that needs to be updated
     */
    @Override
    public void update(Bin toUpdate) {
        String sql = "UPDATE bins SET lat = ?, lng = ?, waste_type = ?, updated_at = ?, photo_url = ?, area = ?, fullness = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, toUpdate.getLatitude());
            ps.setDouble(2, toUpdate.getLongitude());
            ps.setString(3, toUpdate.getCategory().toString());
            setInstant(ps, 4, toUpdate.getUpdated());
            ps.setString(5, toUpdate.getPhotoURL());
            ps.setString(6, toUpdate.getAreaName());
            ps.setString(7, toUpdate.getFullness().toString());
            ps.setInt(8, toUpdate.getID());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for bin id {}", toUpdate.getID());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    private BinFullness convertStringToFullness(String fullness) {
        if (fullness == null)
            return BinFullness.UNKNOWN;

        try {
            return BinFullness.valueOf(fullness.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return BinFullness.UNKNOWN;
        }
    }

   public List<Bin> getBinsAfter(Instant instant) {

        // No known update time
        if (instant == null)
            return getAll();

        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT * FROM bins WHERE updated_at > ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setInstant(ps, 1, instant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bins.add(new Bin(
                            rs.getInt("id"),
                            rs.getDouble("lat"),
                            rs.getDouble("lng"),
                            convertStringToCategory(rs.getString("waste_type")),
                            getInstant(rs, "updated_at"),
                            rs.getString("photo_url"),
                            rs.getString("area"),
                            convertStringToFullness(rs.getString("fullness"))
                    ));
                }
            }
            return bins;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    public void updateFullness(HashMap<Integer, BinFullness> fullness) {

        // Fullness hasnt changed since last check
        if (fullness == null || fullness.isEmpty())
            return;

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
