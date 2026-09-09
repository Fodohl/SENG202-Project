package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.data.Bin;
import seng202.team1.data.TrashCategory;
import seng202.team1.data.TrashType;
import seng202.team1.exceptions.DuplicateEntryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BinDAO implements DAOInterface<Bin>{
    private final DatabaseManager databaseManager;
    private static final Logger log = LogManager.getLogger(BinDAO.class);


    public BinDAO(){databaseManager = DatabaseManager.getInstance();}


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
                        rs.getFloat("lat"),
                        rs.getFloat("long"),
                        rs.getString("photo_url"),
                        convertStringToCategory(rs.getString("waste_type"))));
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
        String sql = "SELECT * FROM bins WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs  = ps.executeQuery()) {
                while (rs.next()) {
                    bin = new Bin(
                            rs.getInt("id"),
                            rs.getFloat("lat"),
                            rs.getFloat("long"),
                            rs.getString("photo_url"),
                            convertStringToCategory(rs.getString("waste_type")));
                }
                return bin;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    @Override
    public int add(Bin toAdd) throws DuplicateEntryException {
        return 0;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Bin toUpdate) {

    }

}
