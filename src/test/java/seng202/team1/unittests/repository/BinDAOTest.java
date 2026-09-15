package seng202.team1.unittests.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.BinDAO;
import seng202.team1.repository.DatabaseManager;

import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class BinDAOTest {
    static BinDAO binDAO;
    static DatabaseManager databaseManager;


    @BeforeAll
    static void setup() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.getInstance();
        binDAO = new BinDAO();
    }

    @BeforeEach
    void resetDB() { databaseManager.resetDB(); }

    @Test
    void testAddBin() {
        Bin toAdd = new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null);
        binDAO.add(toAdd);
        Assertions.assertEquals(1, binDAO.getAll().size());
        Bin bin = binDAO.getAll().getFirst();
        Assertions.assertEquals(toAdd.getID(), bin.getID());
        Assertions.assertEquals(toAdd.getLatitude(), bin.getLatitude());
        Assertions.assertEquals(toAdd.getLongitude(), bin.getLongitude());
        Assertions.assertEquals(toAdd.getCategory(), bin.getCategory());
        Instant expectedUpdate = toAdd.getUpdated().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedUpdate, bin.getUpdated());
        Assertions.assertEquals(toAdd.getFullness(), bin.getFullness());
        Assertions.assertEquals(toAdd.getPhotoURL(), bin.getPhotoURL());
        Assertions.assertEquals(toAdd.getAreaName(), bin.getAreaName());
    }

    @Test
    void testGetBinById() {
        Bin toAdd = new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null);
        int id = binDAO.add(toAdd);

    }
}
