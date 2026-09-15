package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.exceptions.DuplicateEntryException;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BinDAO implements DAOInterface<Bin>{
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(BinDAO.class);


    public BinDAO(){databaseManager = DatabaseManager.getInstance();}


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
     * Converts string from database to Bin Fullness
     * @param category string name
     * @return bin fullness level that matches the string
     */
    private BinFullness convertStringToFullness(String category) {
        return switch(category.toUpperCase(Locale.ROOT)) {
            case "EMPTY" -> BinFullness.EMPTY;
            case "SEMI-FULL" -> BinFullness.SEMI_FULL;
            case "FULL" -> BinFullness.FULL;
            case "OVERFLOWING" -> BinFullness.OVERFLOWING;
            default ->  BinFullness.UNKNOWN;
        };
    }

    @Override
    public List<Bin> getAll() {
        List<Bin> bins = new ArrayList<>();
        String sql = "SELECT * FROM bin";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bins.add(new Bin(
                        rs.getInt("id"),
                        rs.getFloat("lat"),
                        rs.getFloat("long"),
                        convertStringToCategory(rs.getString("waste_type")),
                        rs.getTimestamp("created_at").toInstant(),
                        convertStringToFullness(rs.getString("fullness")),
                        rs.getString("photo_url"),
                        rs.getString("area")));
            }
            return bins;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    @Override
    public Bin getOne(int id) {
        Bin bin = null;
        String sql = "SELECT * FROM bin WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs  = ps.executeQuery()) {
                while (rs.next()) {
                    bin = new Bin(
                            rs.getInt("id"),
                            rs.getFloat("lat"),
                            rs.getFloat("long"),
                            convertStringToCategory(rs.getString("waste_type")),
                            rs.getObject("updated_at", Instant.class),
                            convertStringToFullness(rs.getString("fullness")),
                            rs.getString("photo_url"),
                            rs.getString("area"));
                }
                return bin;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    @Override
    public int add(Bin toAdd) {
        String sql = "INSERT INTO bin (id, lat, lng, waste_type, updated_at, fullness, photo_url, area) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, toAdd.getID());
            ps.setDouble(2, toAdd.getLatitude());
            ps.setDouble(3, toAdd.getLongitude());
            ps.setString(4, toAdd.getCategory().toString());
            ps.setObject(5, toAdd.getUpdated());
            ps.setString(6, toAdd.getFullness().toString());
            ps.setString(7, toAdd.getPhotoURL());
            ps.setString(8, toAdd.getAreaName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int insertId = -1;
            if (rs.next()) {
                insertId = rs.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return -1;
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM bin WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps =conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    @Override
    public void update(Bin toUpdate) {
        String sql = "UPDATE bin SET lat = ?, lng = ?, waste_type = ?, updated_at = ?, fullness = ?, photo_url = ?, area = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, toUpdate.getLatitude());
            ps.setDouble(2, toUpdate.getLongitude());
            ps.setString(3, toUpdate.getCategory().toString());
            ps.setObject(4, toUpdate.getUpdated());
            ps.setString(5, toUpdate.getFullness().toString());
            ps.setString(6, toUpdate.getPhotoURL());
            ps.setString(7, toUpdate.getAreaName());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for trash id {}", toUpdate.getID());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    public void updateBinFullness(BinFullness fullness, int id) {
        String sql = "UPDATE bin SET fullness = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullness.toString());
            ps.setInt(2,  id);
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

}
