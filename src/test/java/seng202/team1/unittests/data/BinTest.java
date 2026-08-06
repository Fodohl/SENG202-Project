package seng202.team1.unittests.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import seng202.team1.data.Bin;
import seng202.team1.data.WasteType;
import seng202.team1.unittests.TrashAppTest;

/**
 * Test Bin
 * @author Stanley Easthope
 */
public class BinTest extends TrashAppTest {

    /**
     * Test that getters work correctly
     */
    @Test
    public void testConstructorAndGetters() {
        Bin bin = new Bin(197, -43.605467593, 172.693149814, WasteType.RECYCLING);

        assertEquals(197, bin.getBinID());
        assertEquals(172.693149814, bin.getBinLongitude());
        assertEquals(-43.605467593, bin.getBinLatitude());
        assertEquals(WasteType.RECYCLING, bin.getBinType());
    }

    /**
     * Test that to string is formatted properly
     */
    @Test
    public void testToString() {
        Bin bin = new Bin(197, -43.605467593, 172.693149814, WasteType.RECYCLING);
        assertEquals("Bin{binID=197, latitude=-43.605467593, longitude=172.693149814, type=RECYCLING}", bin.toString());
    }
}
