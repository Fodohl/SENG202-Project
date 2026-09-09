package seng202.team1.unittests.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.trashlog.Trash;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashLogWriter;
import seng202.team1.data.trashlog.TrashType;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests JSON writing for trash logs.
 */
public class TrashLogWriterTest {

    @TempDir
    Path temporaryDirectory;

    private Path jsonPath;
    private TrashLogWriter writer;

    @BeforeEach
    void setUp() {
        jsonPath = temporaryDirectory.resolve("trashlogs.json");
        writer = new TrashLogWriter();
    }

    @Test
    void writeJsonCreatesFile() throws Exception {
        writer.writeJson(jsonPath, List.of());

        assertTrue(Files.exists(jsonPath));
        assertTrue(Files.size(jsonPath) > 0);
    }

    @Test
    void writeJsonWritesEmptyTrashLogsArray() throws Exception {
        writer.writeJson(jsonPath, List.of());

        JSONObject root = readRoot();
        JSONArray logs = (JSONArray) root.get("trashLogs");

        assertNotNull(logs);
        assertTrue(logs.isEmpty());
    }

    @Test
    void writeJsonWritesOneLogWithRequiredFields() throws Exception {
        TrashLog log = createLog(1, 197);

        writer.writeJson(jsonPath, List.of(log));

        JSONObject root = readRoot();
        JSONArray logs = (JSONArray) root.get("trashLogs");
        JSONObject savedLog = (JSONObject) logs.get(0);
        JSONObject savedTrash =
                (JSONObject) savedLog.get("trashLogged");

        assertEquals(1,
                ((Number) savedLog.get("trashLogId")).intValue());
        assertEquals(197,
                ((Number) savedLog.get("binLogId")).intValue());
        assertEquals("2026-08-14T14:30",
                savedLog.get("disposedAt"));
        assertEquals("2026-08-14T14:35",
                savedLog.get("createdAt"));

        assertEquals("RECYCLABLE",
                savedTrash.get("binCategory"));
        assertEquals("PAPER",
                savedTrash.get("trashType"));
        assertEquals(50,
                ((Number) savedTrash.get("trashWeight")).intValue());
    }

    @Test
    void writeJsonWritesMultipleLogs() throws Exception {
        TrashLog firstLog = createLog(1, 197);
        TrashLog secondLog = createLog(2, 2463);

        writer.writeJson(jsonPath, List.of(firstLog, secondLog));

        JSONObject root = readRoot();
        JSONArray logs = (JSONArray) root.get("trashLogs");

        assertEquals(2, logs.size());

        JSONObject firstSavedLog = (JSONObject) logs.get(0);
        JSONObject secondSavedLog = (JSONObject) logs.get(1);

        assertEquals(1,
                ((Number) firstSavedLog.get("trashLogId")).intValue());
        assertEquals(2,
                ((Number) secondSavedLog.get("trashLogId")).intValue());
    }

    @Test
    void writeJsonIncludesDocumentMetadata() throws Exception {
        writer.writeJson(jsonPath, List.of(createLog(1, 197)));

        JSONObject root = readRoot();

        assertEquals(1,
                ((Number) root.get("schemaVersion")).intValue());
        assertNotNull(root.get("lastSavedAt"));
        assertFalse(root.get("lastSavedAt").toString().isBlank());
    }

    private JSONObject readRoot() throws Exception {
        try (Reader reader = Files.newBufferedReader(
                jsonPath,
                StandardCharsets.UTF_8
        )) {
            return (JSONObject) new JSONParser().parse(reader);
        }
    }

    private TrashLog createLog(int id, int binId) {
        Trash trash = new Trash(
                binCategory.RECYCLABLE,
                TrashType.PAPER,
                50
        );

        return new TrashLog(
                id,
                trash,
                LocalDateTime.parse("2026-08-14T14:30:00"),
                binId,
                LocalDateTime.parse("2026-08-14T14:35:00")
        );
    }
}
