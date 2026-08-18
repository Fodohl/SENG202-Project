package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seng202.team1.data.*;

import java.time.LocalDateTime;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TrashLogLoader
 *
 * @author Marcus Demafeliz
 */
public class TrashLogLoaderTest {
    private TrashLogLoader trashLogLoader;

    @TempDir
    Path temporaryDirectory;

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
        Trash testTrash = new Trash(TrashCategory.E_WASTE, TrashType.OTHER, 2000);
        assertThrows(UnsupportedOperationException.class, () ->
                trashLogs.add(new TrashLog(0,  testTrash, LocalDateTime.parse("2026-04-20T00:00:00"), 1, LocalDateTime.now())));

    }

    @Test
    public void testLogMapsCorrectly() {
        TrashLog trashLog = trashLogLoader.getTrashLogById(1);

        assertEquals(1, trashLog.trashLogId());
        assertEquals(197, trashLog.binLogId());

        assertEquals(TrashCategory.RECYCLABLE,
                trashLog.trashLogged().trashCategory());

        assertEquals(TrashType.PAPER,
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

    @Test
    public void testReadJsonFromExternalFile() throws Exception {
        Path jsonPath = temporaryDirectory.resolve("external-trashlogs.json");

        Files.writeString(
                jsonPath,
                jsonWithOneLog(2, 2463),
                StandardCharsets.UTF_8
        );

        TrashLogLoader externalLoader = new TrashLogLoader(jsonPath);
        externalLoader.readJson();

        TrashLog loadedLog = externalLoader.getTrashLogById(2);

        assertNotNull(loadedLog);
        assertEquals(2463, loadedLog.binLogId());
        assertEquals(
                TrashCategory.RECYCLABLE,
                loadedLog.trashLogged().trashCategory()
        );
        assertEquals(
                TrashType.PAPER,
                loadedLog.trashLogged().trashType()
        );
    }

    @Test
    public void testMissingExternalFileLoadsEmptyHistory() {
        Path missingPath = temporaryDirectory.resolve("does-not-exist.json");

        TrashLogLoader externalLoader = new TrashLogLoader(missingPath);
        externalLoader.readJson();

        assertTrue(externalLoader.getTrashLogs().isEmpty());
    }

    @Test
    public void testReadJsonClearsPreviouslyLoadedLogs() throws Exception {
        Path jsonPath = temporaryDirectory.resolve("trashlogs.json");

        Files.writeString(
                jsonPath,
                jsonWithOneLog(1, 197),
                StandardCharsets.UTF_8
        );

        TrashLogLoader externalLoader = new TrashLogLoader(jsonPath);
        externalLoader.readJson();

        assertNotNull(externalLoader.getTrashLogById(1));

        Files.writeString(
                jsonPath,
                jsonWithOneLog(2, 2463),
                StandardCharsets.UTF_8
        );

        externalLoader.readJson();

        assertEquals(1, externalLoader.getTrashLogs().size());
        assertNull(externalLoader.getTrashLogById(1));
        assertNotNull(externalLoader.getTrashLogById(2));
    }

    @Test
    public void testMalformedExternalFileThrowsError() throws Exception {
        Path jsonPath = temporaryDirectory.resolve("broken-trashlogs.json");

        Files.writeString(
                jsonPath,
                "{ this is not valid JSON",
                StandardCharsets.UTF_8
        );

        TrashLogLoader externalLoader = new TrashLogLoader(jsonPath);

        assertThrows(
                IllegalStateException.class,
                externalLoader::readJson
        );
    }

    private String jsonWithOneLog(int logId, int binId) {
        return """
            {
              "trashLogs": [
                {
                  "trashLogId": %d,
                  "trashLogged": {
                    "trashCategory": "RECYCLABLE",
                    "trashType": "PAPER",
                    "trashWeight": 50
                  },
                  "disposedAt": "2026-08-14T14:30:00",
                  "binLogId": %d,
                  "createdAt": "2026-08-14T14:35:00"
                }
              ]
            }
            """.formatted(logId, binId);
    }

}
