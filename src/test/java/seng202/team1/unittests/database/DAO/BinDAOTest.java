package seng202.team1.unittests.database.DAO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class BinDAOTest extends TrashAppTest {
    
    @Test
    void testAddBin() {
        Bin toAdd = new Bin(2, 1, 1, BinCategory.RECYCLABLE, Instant.now(),null, null, BinFullness.UNKNOWN);
        bins.add(toAdd);
        Assertions.assertEquals(2, bins.getAll().size());
        Bin bin = bins.getAll().getLast();
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
        Bin b1 = new Bin(2, 1, 1, BinCategory.RECYCLABLE, Instant.now(), null, null, BinFullness.UNKNOWN);
        Bin b2 = new Bin(3, 2, 2, BinCategory.ORGANIC, Instant.now(), null, null, BinFullness.UNKNOWN);
        bins.addBatch(new ArrayList<>(Arrays.asList(b1, b2)));
        Assertions.assertEquals(3, bins.getAll().size());
    }

    @Test
    void testGetBinById() {
        Bin toAdd = new Bin(2, 1, 1, BinCategory.RECYCLABLE, Instant.now(), null, null, BinFullness.UNKNOWN);
        int id = bins.add(toAdd);
        Bin returnedBin = bins.getOne(id);
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
        int id = bins.add(new Bin(2, 1, 1, BinCategory.RECYCLABLE, Instant.now(), null, null, BinFullness.UNKNOWN));
        int total = bins.getAll().size();
        bins.delete(id);
        Assertions.assertEquals(total - 1, bins.getAll().size());
    }

    @Test
    void testUpdateBin() {
        Bin bin = new Bin(2, 1, 1, BinCategory.RECYCLABLE, Instant.now(),null, null, BinFullness.UNKNOWN);
        int id = bins.add(bin);
        Bin updatedBin = new Bin(id, 2, 2, BinCategory.ORGANIC, Instant.now(),null, null, BinFullness.UNKNOWN);
        bins.update(updatedBin);
        Bin result = bins.getOne(id);
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
}
