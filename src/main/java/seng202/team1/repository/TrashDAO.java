package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.Trash;
import seng202.team1.data.TrashCategory;
import seng202.team1.data.TrashType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Database Access Object that handles all trash related actions to the databse
 *
 * @author Marcus Demafeliz
 */
public class TrashDAO implements DAOInterface<Trash> {
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(TrashDAO.class);

    /**
     * Creates a new TrashDAo object and gets a reference to the database singleton
     */
    public TrashDAO(){databaseManager = DatabaseManager.getInstance(); }

    /**
     * Converts string from database to TrashCategory
     * @param category string name
     * @return trash category that matches the string
     */
    private TrashCategory convertStringToCategory(String category) {
        return switch(category.toUpperCase(Locale.ROOT)) {
            case "RECYCLABLE" -> TrashCategory.RECYCLABLE;
            case "ORGANIC" -> TrashCategory.ORGANIC;
            case "E-WASTE", "E_WASTE" -> TrashCategory.E_WASTE;
            default ->  TrashCategory.GENERAL_WASTE;
        };
    }

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
     * Gets all trash in database
     *
     * @return a list of all trash
     */
    @Override
    public List<Trash> getAll() {
        List<Trash> trash = new ArrayList<>();
        String sql = "SELECT * FROM trash";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trash.add(new Trash(
                        rs.getInt("id"),
                        convertStringToCategory(rs.getString("category")),
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
     * Gets individual trash from database by id
     * @param id id of object to get
     * @return Trash trash from database that matches id
     */
    @Override
    public Trash getOne(int id) {
        Trash trash = null;
        String sql = "SELECT * FROM trash WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trash = new Trash(
                            rs.getInt("id"),
                            convertStringToCategory(rs.getString("category")),
                            convertStringToType(rs.getString("type")),
                            rs.getInt("weight"));
                }
                return trash;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Adds an individual trash to database
     * @param trashToAdd object of type T to add
     * @return true if no error, false if sql error
     */
    @Override
    public int add(Trash trashToAdd) {
        String sql = "INSERT INTO trash (category, type, weight) VALUES (?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, trashToAdd.trashCategory().toString());
            ps.setString(2, trashToAdd.trashType().toString());
            ps.setInt(3, trashToAdd.trashWeight());

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
     * Delete trash from database by id
     * @param id id of object to delete
     */
    @Override
    public void delete (int id) {
        String sql = "DELETE FROM trash WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    @Override
    public void update(Trash trashToUpdate) {
        String sql = "UPDATE trash SET category = ?, type = ?, weight = ? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setString(1, trashToUpdate.trashCategory().toString());
            ps.setString(2, trashToUpdate.trashType().toString());
            ps.setInt(3, trashToUpdate.trashWeight());
            ps.setInt(4, trashToUpdate.id());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("Update affected 0 rows for trash id {}", trashToUpdate.id());
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

}

