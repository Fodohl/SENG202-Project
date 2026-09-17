package seng202.team1.unittests.data.bin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;

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
        Bin bin = new Bin(197, -43.605467593, 172.693149814, BinCategory.RECYCLABLE, Instant.now(), "Cloud");

        assertEquals(197, bin.getID());
        assertEquals(-43.605467593, bin.getLatitude());
        assertEquals(172.693149814, bin.getLongitude());
        assertEquals(BinCategory.RECYCLABLE, bin.getCategory());
    }

    /**
     * Test that to string is formatted properly
     */
    @Test
    public void testToString() {
        Bin bin = new Bin(197, -43.605467593, 172.693149814, BinCategory.RECYCLABLE,  Instant.now(), "Cloud");
        assertEquals("Bin{binID=197, latitude=-43.605467593, longitude=172.693149814, type=RECYCLABLE}", bin.toString());
    }
}
