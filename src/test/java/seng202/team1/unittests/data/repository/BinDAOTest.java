package seng202.team1.unittests.data.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.data.repository.BinDAO;
import seng202.team1.data.repository.DatabaseManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class BinDAOTest {
    static BinDAO binDAO;
    static DatabaseManager databaseManager;


    @BeforeAll
    static void setup() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
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
    void testAddBatchBin() {
        Bin b1 = new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null);
        Bin b2 = new Bin(2, 2, 2, BinCategory.ORGANIC, Instant.now(), BinFullness.OVERFLOWING,null, null);
        binDAO.addBatch(new ArrayList<>(Arrays.asList(b1, b2)));
        Assertions.assertEquals(2, binDAO.getAll().size());
    }

    @Test
    void testGetBinById() {
        Bin toAdd = new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null);
        int id = binDAO.add(toAdd);
        Bin returnedBin = binDAO.getOne(id);
        Assertions.assertEquals(toAdd.getID(), returnedBin.getID());
        Assertions.assertEquals(toAdd.getLatitude(), returnedBin.getLatitude());
        Assertions.assertEquals(toAdd.getLongitude(), returnedBin.getLongitude());
        Assertions.assertEquals(toAdd.getCategory(), returnedBin.getCategory());
        Instant expectedUpdate = toAdd.getUpdated().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedUpdate, returnedBin.getUpdated());
        Assertions.assertEquals(toAdd.getFullness(), returnedBin.getFullness());
        Assertions.assertEquals(toAdd.getPhotoURL(), returnedBin.getPhotoURL());
        Assertions.assertEquals(toAdd.getAreaName(), returnedBin.getAreaName());
    }

    @Test
    void testDeleteBin() {
        int id = binDAO.add(new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null));
        int total = binDAO.getAll().size();
        binDAO.delete(id);
        Assertions.assertEquals(total - 1, binDAO.getAll().size());
    }

    @Test
    void testUpdateBin() {
        Bin bin = new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null);
        int id = binDAO.add(bin);
        Bin updatedBin = new Bin(id, 2, 2, BinCategory.ORGANIC, Instant.now(), BinFullness.OVERFLOWING,null, null);
        binDAO.update(updatedBin);
        Bin result = binDAO.getOne(id);
        Assertions.assertEquals(updatedBin.getID(), result.getID());
        Assertions.assertEquals(updatedBin.getLatitude(), result.getLatitude());
        Assertions.assertEquals(updatedBin.getLongitude(), result.getLongitude());
        Assertions.assertEquals(updatedBin.getCategory(), result.getCategory());

        Instant expectedUpdate = updatedBin.getUpdated().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedUpdate, result.getUpdated());

        Assertions.assertEquals(updatedBin.getFullness(), result.getFullness());
        Assertions.assertEquals(updatedBin.getPhotoURL(), result.getPhotoURL());
        Assertions.assertEquals(updatedBin.getAreaName(), result.getAreaName());

    }

    @Test
    void testUpdateBinFullness() {
        Bin bin = new Bin(1, 1, 1, BinCategory.RECYCLABLE, Instant.now(), BinFullness.EMPTY,null, null);
        int id = binDAO.add(bin);
        BinFullness newFullness = BinFullness.FULL;
        binDAO.updateBinFullness(newFullness, id);
        Bin result = binDAO.getOne(id);
        Assertions.assertEquals(newFullness, result.getFullness());
    }
}
