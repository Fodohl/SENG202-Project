package seng202.team1.unittests.data.bin;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
import seng202.team1.data.Bin;
import seng202.team1.data.DataLoader;
import seng202.team1.data.binCategory;
=======
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.unittests.TrashAppTest;
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java

/**
 * Test binLoader
 * @author Stanley Easthope
 */
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
public class DataLoaderTest {
    private BinLoader binLoader;

    /**
     * Setup before each test
     * Gets a new read from the GeoJSON file
     */
    @BeforeEach
    public void setupTest() {
        binLoader = new BinLoader();
        binLoader.readGeoJSON();
    }
=======
public class BinDatasetTest extends TrashAppTest {


>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java

    /**
     * Check that the bins aren't empty
     */
    @Test
    public void testReadGeoJSONPopulatesBins() {
        assertFalse(binDataset.getBinDataset().isEmpty());
    }

    /**
     * Test that the list can't be changed
     */
    @Test
    public void testGetBinObjectsIsNotChangable() {
        List<Bin> bins = binDataset.getBinDataset();
        assertThrows(UnsupportedOperationException.class, () ->
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
                bins.add(new Bin(0, 0, 0, WasteType.GENERAL_WASTE)));
=======
                bins.add(new Bin(0, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(),null, null, BinFullness.UNKNOWN)));
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java
    }

    /**
     * Test that a recycling bin is assigned to the right type
     */
    @Test
    public void testRecyclingBin() {
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
        Bin bin = dataLoader.getBinById(2463);
        assertEquals(WasteType.RECYCLING, bin.getBinType());
=======
        Bin bin = binDataset.getBinById(2463);
        assertEquals(BinCategory.RECYCLABLE, bin.getCategory());
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java
    }

    /**
     * Test that a compost bin is assigned to the right type
     */
    @Test
    public void testCompostBinMappedCorrectly() {
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
        Bin bin = dataLoader.getBinById(2537);
        assertEquals(WasteType.ORGANIC, bin.getBinType());
=======
        Bin bin = binDataset.getBinById(2537);
        assertEquals(BinCategory.ORGANIC, bin.getCategory());
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java
    }

    /**
     * Test that a general waste bin is the fallback type when the waste type is unknown
     */
    @Test
    public void testGeneralWasteBinMappedCorrectly() {
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
        Bin bin = dataLoader.getBinById(197);
        assertEquals(WasteType.GENERAL_WASTE, bin.getBinType());
=======
        Bin bin = binDataset.getBinById(197);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java
    }

    /**
     * Test that a Canine littler waste bin maps to general waste
     */
    @Test
    public void testCanineLittlerBinMappedToGeneralWaste() {
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
        Bin bin = dataLoader.getBinById(63534);
        assertEquals(WasteType.GENERAL_WASTE, bin.getBinType());
=======
        Bin bin = binDataset.getBinById(63534);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java
    }

    /**
     * Test that a litter and recycling waste bin maps to general waste
     */
    @Test
    public void testLitterAndRecyclingBinMappedToGeneralWaste() {
<<<<<<< HEAD:src/test/java/seng202/team1/unittests/data/DataLoaderTest.java
        Bin bin = dataLoader.getBinById(311361);
        assertEquals(WasteType.GENERAL_WASTE, bin.getBinType());
=======
        Bin bin = binDataset.getBinById(311361);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
>>>>>>> main:src/test/java/seng202/team1/unittests/data/bin/BinDatasetTest.java
    }
}
