package seng202.team1.unittests.data_managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seng202.team1.data.Bin;
import seng202.team1.data.DataLoader;
import seng202.team1.data.TrashCategory;

/**
 * Test DataLoader
 * @author Stanley Easthope
 */
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

    /**
     * Check that the bins aren't empty
     */
    @Test
    public void testReadGeoJSONPopulatesBins() {
        assertFalse(dataLoader.getBinObjects().isEmpty());
    }

    /**
     * Test that the list can't be changed
     */
    @Test
    public void testGetBinObjectsIsNotChangable() {
        List<Bin> bins = dataLoader.getBinObjects();
        assertThrows(UnsupportedOperationException.class, () ->
                bins.add(new Bin(0, 0, 0, WasteType.GENERAL_WASTE)));
    }

    /**
     * Test that a recycling bin is assigned to the right type
     */
    @Test
    public void testRecyclingBin() {
        Bin bin = dataLoader.getBinById(2463);
        assertEquals(WasteType.RECYCLING, bin.getBinType());
    }

    /**
     * Test that a compost bin is assigned to the right type
     */
    @Test
    public void testCompostBinMappedCorrectly() {
        Bin bin = dataLoader.getBinById(2537);
        assertEquals(WasteType.ORGANIC, bin.getBinType());
    }

    /**
     * Test that a general waste bin is the fallback type when the waste type is unknown
     */
    @Test
    public void testGeneralWasteBinMappedCorrectly() {
        Bin bin = dataLoader.getBinById(197);
        assertEquals(WasteType.GENERAL_WASTE, bin.getBinType());
    }

    /**
     * Test that a Canine littler waste bin maps to general waste
     */
    @Test
    public void testCanineLittlerBinMappedToGeneralWaste() {
        Bin bin = dataLoader.getBinById(63534);
        assertEquals(WasteType.GENERAL_WASTE, bin.getBinType());
    }

    /**
     * Test that a litter and recycling waste bin maps to general waste
     */
    @Test
    public void testLitterAndRecyclingBinMappedToGeneralWaste() {
        Bin bin = dataLoader.getBinById(311361);
        assertEquals(WasteType.GENERAL_WASTE, bin.getBinType());
    }
}
