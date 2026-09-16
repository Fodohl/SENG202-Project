package seng202.team1.unittests.data.trashlog;

import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.Trash;
import seng202.team1.data.trashlog.TrashType;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test TrashTest
 * @author Marcus Demafeliz
 */
public class TrashTest {

    @Test
    public void testGetTrashWithSpecifiedWeight() {
        Trash testTrash = new Trash(BinCategory.E_WASTE, TrashType.OTHER, 2000);
        assertEquals(2000, testTrash.trashWeight());
    }

    @Test
    public void testGetTrashWithNoSpecifiedWeight() {
        Trash testTrash = new Trash(BinCategory.GENERAL_WASTE, TrashType.OTHER);
        assertEquals(0, testTrash.trashWeight());
    }
}
