package seng202.team1.unittests.data.trashlog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.Trash;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashLogStore;
import seng202.team1.data.trashlog.TrashType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests persistence and retrieval of trash logs.
 */
public class TrashLogStoreTest {

    @TempDir
    Path temporaryDirectory;

    private Path jsonPath;
    private TrashLogStore store;

    @BeforeEach
    void setUp() {
        jsonPath = temporaryDirectory.resolve("trashlogs.json");
        store = new TrashLogStore(jsonPath);
    }

    @Test
    void loadMissingFileCreatesEmptyHistory() {
        store.load();

        assertTrue(store.getAll().isEmpty());
    }

    @Test
    void addCreatesJsonFileAndStoresLog() throws Exception {
        TrashLog log = createLog(
                1,
                197,
                "2026-08-14T14:30:00"
        );

        store.load();
        store.add(log);

        assertTrue(Files.exists(jsonPath));
        assertEquals(List.of(log), store.getAll());
    }

    @Test
    void addPreservesExistingLogs() throws Exception {
        TrashLog firstLog = createLog(
                1,
                197,
                "2026-08-14T14:30:00"
        );

        TrashLog secondLog = createLog(
                2,
                2463,
                "2026-08-14T15:30:00"
        );

        store.load();
        store.add(firstLog);
        store.add(secondLog);

        assertEquals(List.of(firstLog, secondLog), store.getAll());
    }

    @Test
    void savedLogsReloadInNewStoreInstance() throws Exception {
        TrashLog firstLog = createLog(
                1,
                197,
                "2026-08-14T14:30:00"
        );

        TrashLog secondLog = createLog(
                2,
                2463,
                "2026-08-14T15:30:00"
        );

        store.load();
        store.add(firstLog);
        store.add(secondLog);

        TrashLogStore reopenedStore = new TrashLogStore(jsonPath);
        reopenedStore.load();

        assertEquals(
                List.of(firstLog, secondLog),
                reopenedStore.getAll()
        );
    }

    @Test
    void addRejectsDuplicateLogId() throws Exception {
        TrashLog firstLog = createLog(
                1,
                197,
                "2026-08-14T14:30:00"
        );

        TrashLog duplicateIdLog = createLog(
                1,
                2463,
                "2026-08-14T15:30:00"
        );

        store.load();
        store.add(firstLog);

        assertThrows(
                IllegalArgumentException.class,
                () -> store.add(duplicateIdLog)
        );
    }

    @Test
    void nextIdIsOneHigherThanLargestCurrentId() throws Exception {
        TrashLog logWithIdFour = createLog(
                4,
                197,
                "2026-08-14T14:30:00"
        );

        TrashLog logWithIdSeven = createLog(
                7,
                2463,
                "2026-08-14T15:30:00"
        );

        store.load();
        store.add(logWithIdFour);
        store.add(logWithIdSeven);

        assertEquals(8, store.nextId());
    }

    @Test
    void findByBinIdReturnsOnlyMatchingLogsInTimeOrder()
            throws Exception {

        TrashLog laterMatchingLog = createLog(
                1,
                197,
                "2026-08-14T16:30:00"
        );

        TrashLog otherBinLog = createLog(
                2,
                2463,
                "2026-08-14T15:30:00"
        );

        TrashLog earlierMatchingLog = createLog(
                3,
                197,
                "2026-08-14T14:30:00"
        );

        store.load();
        store.add(laterMatchingLog);
        store.add(otherBinLog);
        store.add(earlierMatchingLog);

        assertEquals(
                List.of(earlierMatchingLog, laterMatchingLog),
                store.findByBinId(197)
        );
    }

    @Test
    void findByDateRangeIncludesStartAndExcludesEnd()
            throws Exception {

        TrashLog startLog = createLog(
                1,
                197,
                "2026-08-14T14:00:00"
        );

        TrashLog middleLog = createLog(
                2,
                197,
                "2026-08-14T15:00:00"
        );

        TrashLog endLog = createLog(
                3,
                197,
                "2026-08-14T16:00:00"
        );

        store.load();
        store.add(startLog);
        store.add(middleLog);
        store.add(endLog);

        List<TrashLog> results = store.findByDateRange(
                LocalDateTime.parse("2026-08-14T14:00:00"),
                LocalDateTime.parse("2026-08-14T16:00:00")
        );

        assertEquals(List.of(startLog, middleLog), results);
    }

    @Test
    void findByBinAndDateRangeFiltersByBothValues()
            throws Exception {

        TrashLog matchingLog = createLog(
                1,
                197,
                "2026-08-14T14:30:00"
        );

        TrashLog wrongBinLog = createLog(
                2,
                2463,
                "2026-08-14T14:30:00"
        );

        TrashLog outsideDateLog = createLog(
                3,
                197,
                "2026-08-14T17:30:00"
        );

        store.load();
        store.add(matchingLog);
        store.add(wrongBinLog);
        store.add(outsideDateLog);

        List<TrashLog> results = store.findByBinAndDateRange(
                197,
                LocalDateTime.parse("2026-08-14T14:00:00"),
                LocalDateTime.parse("2026-08-14T16:00:00")
        );

        assertEquals(List.of(matchingLog), results);
    }

    @Test
    void findByDateRangeRejectsInvalidRange() {
        assertThrows(
                IllegalArgumentException.class,
                () -> store.findByDateRange(
                        LocalDateTime.parse("2026-08-14T16:00:00"),
                        LocalDateTime.parse("2026-08-14T14:00:00")
                )
        );
    }

    private TrashLog createLog(
            int id,
            int binId,
            String disposedAt
    ) {
        Trash trash = new Trash(
                BinCategory.RECYCLABLE,
                TrashType.PAPER,
                50
        );

        LocalDateTime disposalTime = LocalDateTime.parse(disposedAt);

        return new TrashLog(
                id,
                trash,
                disposalTime,
                binId,
                disposalTime.plusMinutes(5)
        );
    }
}