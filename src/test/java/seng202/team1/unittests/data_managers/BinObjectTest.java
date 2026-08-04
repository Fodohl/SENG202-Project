package seng202.team1.unittests.data_managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import seng202.team1.data.Bin;

/**
 * Test BinObject
 * @author Stanley Easthope
 */
public class BinObjectTest {

    /**
     * Test that getters work correctly
     */
    @Test
    public void testConstructorAndGetters() {
        Bin bin = new Bin(197, -43.605467593, 172.693149814, Bin.BinType.RECYCLING);

        assertEquals(197, bin.getBinID());
        assertEquals(172.693149814, bin.getBinLongitude());
        assertEquals(-43.605467593, bin.getBinLatitude());
        assertEquals(Bin.BinType.RECYCLING, bin.getBinType());
    }
}
