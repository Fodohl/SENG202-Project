package seng202.team1.unittests.data_managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seng202.team1.data_managers.BinObject;
import seng202.team1.data_managers.DataLoader;

/**
 * Test DataLoader
 * @author Stanley Easthope
 */
public class DataLoaderTest {
    private DataLoader dataLoader;

    /**
     * Setup before each test
     * Gets a new read from the GeoJSON file
     */
    @BeforeEach
    public void setupTest() {
        dataLoader = new DataLoader();
        dataLoader.readGeoJSON();
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
        List<BinObject> bins = dataLoader.getBinObjects();
        assertThrows(UnsupportedOperationException.class, () ->
                bins.add(new BinObject(0, 0, 0, BinObject.BinType.GENERAL_WASTE)));
    }

    /**
     * Test that a recycling bin is assigned to the right type
     */
    @Test
    public void testRecyclingBin() {
        BinObject bin = dataLoader.getBinById(2463);
        assertEquals(BinObject.BinType.RECYCLING, bin.getBinType());
    }

    /**
     * Test that a compost bin is assigned to the right type
     */
    @Test
    public void testCompostBinMappedCorrectly() {
        BinObject bin = dataLoader.getBinById(2537);
        assertEquals(BinObject.BinType.ORGANIC, bin.getBinType());
    }

    /**
     * Test that a general waste bin is the fallback type when the waste type is unknown
     */
    @Test
    public void testGeneralWasteBinMappedCorrectly() {
        BinObject bin = dataLoader.getBinById(197);
        assertEquals(BinObject.BinType.GENERAL_WASTE, bin.getBinType());
    }
}
