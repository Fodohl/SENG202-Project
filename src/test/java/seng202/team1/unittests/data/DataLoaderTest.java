package seng202.team1.unittests.data;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seng202.team1.data.Bin;
import seng202.team1.data.DataLoader;
import seng202.team1.unittests.TrashAppTest;

/**
 * Test DataLoader
 * @author Stanley Easthope
 */
public class DataLoaderTest extends TrashAppTest {

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
                bins.add(new Bin(0, 0, 0, Bin.BinType.GENERAL_WASTE)));
    }

    /**
     * Test that a recycling bin is assigned to the right type
     */
    @Test
    public void testRecyclingBin() {
        Bin bin = dataLoader.getBinById(2463);
        assertEquals(Bin.BinType.RECYCLING, bin.getBinType());
    }

    /**
     * Test that a compost bin is assigned to the right type
     */
    @Test
    public void testCompostBinMappedCorrectly() {
        Bin bin = dataLoader.getBinById(2537);
        assertEquals(Bin.BinType.ORGANIC, bin.getBinType());
    }

    /**
     * Test that a general waste bin is the fallback type when the waste type is unknown
     */
    @Test
    public void testGeneralWasteBinMappedCorrectly() {
        Bin bin = dataLoader.getBinById(197);
        assertEquals(Bin.BinType.GENERAL_WASTE, bin.getBinType());
    }
}
