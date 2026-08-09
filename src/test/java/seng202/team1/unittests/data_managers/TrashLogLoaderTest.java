package seng202.team1.unittests.data_managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data_managers.Trash;
import seng202.team1.data_managers.TrashLog;
import seng202.team1.data_managers.TrashLogLoader;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TrashLogLoader
 *
 * @author Marcus Demafeliz
 */
public class TrashLogLoaderTest {
    private TrashLogLoader trashLogLoader;

    @BeforeEach
    public void setUp() {
        trashLogLoader = new TrashLogLoader();
        trashLogLoader.readJson();
    }

    @Test
    public void testReadJsonPopulateLogs() {
        assertFalse(trashLogLoader.getTrashLogs().isEmpty());
    }

    @Test
    public void testGetLogsIsNotChangeable() {
        List<TrashLog> trashLogs = trashLogLoader.getTrashLogs();
        Trash testTrash = new Trash(Trash.TrashCategory.E_WASTE, Trash.TrashType.OTHER, 2000);
        assertThrows(UnsupportedOperationException.class, () ->
                trashLogs.add(new TrashLog(0,  testTrash, LocalDateTime.parse("2026-04-20T00:00:00"), 1, LocalDateTime.now())));

    }

    @Test
    public void testLogMapsCorrectly() {
        TrashLog trashLog = trashLogLoader.getTrashLogById(1);

        assertEquals(1, trashLog.trashLogId());
        assertEquals(197, trashLog.binLogId());

        assertEquals(Trash.TrashCategory.RECYCLABLE,
                trashLog.trashLogged().trashCategory());

        assertEquals(Trash.TrashType.PAPER,
                trashLog.trashLogged().trashType());

        assertEquals(50,
                trashLog.trashLogged().trashWeight());

        assertEquals(
                LocalDateTime.parse("2026-04-20T00:00:00"),
                trashLog.disposedAt()
        );

        assertEquals(
                LocalDateTime.parse("2026-08-09T00:00:00"),
                trashLog.createdAt()
        );
    }

}
