package seng202.team1.unittests.data.bin;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.unittests.TrashAppTest;

/**
 * Test binLoader
 * @author Stanley Easthope
 */
public class BinDatasetTest extends TrashAppTest {



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
                bins.add(new Bin(0, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(),null, null, BinFullness.UNKNOWN)));
    }

    /**
     * Test that a recycling bin is assigned to the right type
     */
    @Test
    public void testRecyclingBin() {
        Bin bin = binDataset.getBinById(2463);
        assertEquals(BinCategory.RECYCLABLE, bin.getCategory());
    }

    /**
     * Test that a compost bin is assigned to the right type
     */
    @Test
    public void testCompostBinMappedCorrectly() {
        Bin bin = binDataset.getBinById(2537);
        assertEquals(BinCategory.ORGANIC, bin.getCategory());
    }

    /**
     * Test that a general waste bin is the fallback type when the waste type is unknown
     */
    @Test
    public void testGeneralWasteBinMappedCorrectly() {
        Bin bin = binDataset.getBinById(197);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
    }

    /**
     * Test that a Canine littler waste bin maps to general waste
     */
    @Test
    public void testCanineLittlerBinMappedToGeneralWaste() {
        Bin bin = binDataset.getBinById(63534);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
    }

    /**
     * Test that a litter and recycling waste bin maps to general waste
     */
    @Test
    public void testLitterAndRecyclingBinMappedToGeneralWaste() {
        Bin bin = binDataset.getBinById(311361);
        assertEquals(BinCategory.GENERAL_WASTE, bin.getCategory());
    }
}
