package seng202.team1.unittests.data_managers;

import org.junit.jupiter.api.Test;
import seng202.team1.data_managers.Trash;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test TrashTest
 * @author Marcus Demafeliz
 */
public class TrashTest {

    @Test
    public void testGetTrashWithSpecifiedWeight() {
        Trash testTrash = new Trash(Trash.TrashCategory.E_WASTE, Trash.TrashType.OTHER, 2000);
        assertEquals(2000, testTrash.trashWeight());
    }

    @Test
    public void testGetTrashWithNoSpecifiedWeight() {
        Trash testTrash = new Trash(Trash.TrashCategory.GENERAL_WASTE, Trash.TrashType.OTHER);
        assertEquals(0, testTrash.trashWeight());
    }
}
