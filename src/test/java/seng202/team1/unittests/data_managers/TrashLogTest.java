package seng202.team1.unittests.data_managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data_managers.Trash;
import seng202.team1.data_managers.TrashLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test TrashLog
 * @author Marcus Demafeliz
 */
public class TrashLogTest {

    private TrashLog testLog;

    @BeforeEach
    public void testSetup() {
        Trash testTrash = new Trash(Trash.TrashCategory.GENERAL_WASTE, Trash.TrashType.PLASTIC_BOTTLE, 15);
        testLog = new TrashLog(1, testTrash,  LocalDateTime.parse("2026-04-20T00:00:00"), 1);
    }

    @Test
    public void testGetCreatedAtLog() {
        assertEquals(LocalDate.now(), testLog.getCreatedAt().toLocalDate());
    }

    @Test
    public void testGetDisposedAtLog() {
        assertEquals(LocalDateTime.parse("2026-04-20T00:00:00"), testLog.getDisposedAt());
    }

    @Test
    public void testGetTrashLogTrashWeight() {
        assertEquals(15, testLog.getTrashLogged().trashWeight());
    }
}
