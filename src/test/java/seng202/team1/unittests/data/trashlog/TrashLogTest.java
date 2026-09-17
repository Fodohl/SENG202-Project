package seng202.team1.unittests.data.trashlog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.Trash;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;

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
        Trash testTrash = new Trash(BinCategory.GENERAL_WASTE, TrashType.PLASTIC_BOTTLE, 15);
        testLog = new TrashLog(1, testTrash,  LocalDateTime.parse("2026-04-20T00:00:00"), 1, LocalDateTime.now());
    }

    @Test
    public void testGetCreatedAtLog() {
        assertEquals(LocalDate.now(), testLog.createdAt().toLocalDate());
    }

    @Test
    public void testGetDisposedAtLog() {
        assertEquals(LocalDateTime.parse("2026-04-20T00:00:00"), testLog.disposedAt());
    }

    @Test
    public void testGetTrashLogTrashWeight() {
        assertEquals(15, testLog.trashLogged().trashWeight());
    }
}
