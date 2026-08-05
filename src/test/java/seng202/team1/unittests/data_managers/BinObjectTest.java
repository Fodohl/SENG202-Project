package seng202.team1.unittests.data_managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import seng202.team1.data_managers.BinObject;

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
        BinObject bin = new BinObject(197, -43.605467593, 172.693149814, BinObject.BinType.RECYCLING);

        assertEquals(197, bin.getBinID());
        assertEquals(172.693149814, bin.getBinLongitude());
        assertEquals(-43.605467593, bin.getBinLatitude());
        assertEquals(BinObject.BinType.RECYCLING, bin.getBinType());
    }

    /**
     * Test that to string is formatted properly
     */
    @Test
    public void testToString() {
        BinObject bin = new BinObject(197, -43.605467593, 172.693149814, BinObject.BinType.RECYCLING);
        assertEquals("BinObject{binID=197, latitude=-43.605467593, longitude=172.693149814, type=RECYCLING}", bin.toString());
    }
}
