package seng202.team1.unittests.data;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.bin.BinCategory;

/**
 * Test DataLoader
 * @author Stanley Easthope
 */
public class BinLoaderTest {
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

    /**
     * Check that the bins aren't empty
     */
    @Test
    public void testReadGeoJSONPopulatesBins() {
        assertFalse(binLoader.getBinObjects().isEmpty());
    }

    /**
     * Test that the list can't be changed
     */
    @Test
    public void testGetBinObjectsIsNotChangable() {
        List<Bin> bins = binLoader.getBinObjects();
        assertThrows(UnsupportedOperationException.class, () ->
                bins.add(new Bin(0, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), BinFullness.EMPTY, null, null)));
    }

    /**
     * Test that a recycling bin is assigned to the right type
     */
    @Test
    public void testRecyclingBin() {
        Bin bin = binLoader.getBinById(2463);
        assertEquals(BinCategory.RECYCLABLE, bin.getCategory());
    }

    /**
     * Test that a compost bin is assigned to the right type
     */
    @Test
    public void testCompostBinMappedCorrectly() {
        Bin bin = binLoader.getBinById(2537);
        assertEquals(BinCategory.ORGANIC, bin.getCategory());
    }

    /**
     * Test that a general waste bin is the fallback type when the waste type is unknown
     */
    @Test
    public void testGeneralWasteBinMappedCorrectly() {
        Bin bin = binLoader.getBinById(197);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
    }

    /**
     * Test that a Canine littler waste bin maps to general waste
     */
    @Test
    public void testCanineLittlerBinMappedToGeneralWaste() {
        Bin bin = binLoader.getBinById(63534);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
    }

    /**
     * Test that a litter and recycling waste bin maps to general waste
     */
    @Test
    public void testLitterAndRecyclingBinMappedToGeneralWaste() {
        Bin bin = binLoader.getBinById(311361);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
    }
}
