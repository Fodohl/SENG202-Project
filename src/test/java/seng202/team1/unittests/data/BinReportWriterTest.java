package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.bin.BinIssue;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.User;
import seng202.team1.data.status.StatusReportWriter;
import seng202.team1.data.bin.BinStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests StatusReportWriter
 *
 * @author connorrocket
 * @author Archie Georgeson
 */

public class StatusReportWriterTest {
    private StatusReportWriter statusReportWriter;
    private StatusReport testStatusReport;
    private StatusReport testStatusReport2;
    private Bin testBin;
    private User testUser;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        statusReportWriter = new StatusReportWriter();

        testBin = new Bin(
                1,
                1.0,
                1.0,
                TrashCategory.GENERAL_WASTE
        );

        testUser = new User(1, "Test");

        testStatusReport = new StatusReport(
                testBin,
                testUser,
                BinStatus.FULL,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.of(2026, 9, 9, 12, 0)
        );

         testStatusReport2 = new StatusReport(
                testBin,
                new User(2, "Test2"),
                BinStatus.EMPTY,
                BinIssue.GRAFFITI,
                "Test description2",
                LocalDateTime.of(2026, 9, 9, 12, 0)
        );
    }

    @Test
    public void testWriteJsonCreatesFile() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");

        statusReportWriter.writeJson(testFile, List.of(testStatusReport));

        assertTrue(Files.exists(testFile));
    }

    @Test
    public void testWriteJsonContainsReport() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");

        statusReportWriter.writeJson(testFile, List.of(testStatusReport));

        String contents = Files.readString(testFile);

        assertTrue(contents.contains("\"binID\":1"));
        assertTrue(contents.contains("\"user\":\"Test\""));
        assertTrue(contents.contains("\"userID\":1"));
        assertTrue(contents.contains("\"status\":\"FULL\""));
        assertTrue(contents.contains("\"issue\":\"OTHER\""));
        assertTrue(contents.contains("\"description\":\"Test description\""));
    }

    @Test
    public void testEmptyListWritesEmptyJson() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");
        statusReportWriter.writeJson(testFile, List.of());
        assertTrue(Files.exists(testFile));
        String contents = Files.readString(testFile);
        assertTrue(contents.contains("\"statusReports\":[]"));
        assertFalse(contents.contains("\"binID\""));
        assertFalse(contents.contains("\"user\""));
        assertFalse(contents.contains("\"userID\""));
        assertFalse(contents.contains("\"status\""));
        assertFalse(contents.contains("\"issue\""));
        assertFalse(contents.contains("\"description\""));
    }

    @Test
    public void testWriteJsonContainsMultipleReports() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");
        statusReportWriter.writeJson(testFile, List.of(testStatusReport, testStatusReport2));

        String contents = Files.readString(testFile);

        assertTrue(contents.contains("\"binID\":1"));
        assertTrue(contents.contains("\"user\":\"Test\""));
        assertTrue(contents.contains("\"userID\":1"));
        assertTrue(contents.contains("\"status\":\"FULL\""));
        assertTrue(contents.contains("\"issue\":\"OTHER\""));
        assertTrue(contents.contains("\"description\":\"Test description\""));

        assertTrue(contents.contains("\"binID\":1"));
        assertTrue(contents.contains("\"user\":\"Test2\""));
        assertTrue(contents.contains("\"userID\":2"));
        assertTrue(contents.contains("\"status\":\"EMPTY\""));
        assertTrue(contents.contains("\"issue\":\"GRAFFITI\""));
        assertTrue(contents.contains("\"description\":\"Test description2\""));
    }

    @Test
    public void writeJsonOverwritesOldJson() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");
        statusReportWriter.writeJson(testFile, List.of(testStatusReport));
        statusReportWriter.writeJson(testFile, List.of(testStatusReport2));
        String contents = Files.readString(testFile);
        assertTrue(contents.contains("\"binID\":1"));
        assertTrue(contents.contains("\"user\":\"Test2\""));
        assertTrue(contents.contains("\"userID\":2"));
        assertTrue(contents.contains("\"status\":\"EMPTY\""));
        assertTrue(contents.contains("\"issue\":\"GRAFFITI\""));
        assertTrue(contents.contains("\"description\":\"Test description2\""));

        assertFalse(contents.contains("\"user\":\"Test\""));
        assertFalse(contents.contains("\"userID\":1"));
        assertFalse(contents.contains("\"status\":\"FULL\""));
        assertFalse(contents.contains("\"issue\":\"OTHER\""));
        assertFalse(contents.contains("\"description\":\"Test description\""));
    }

    @Test
    public void writeJsonWritesNull() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");
        StatusReport nullReport = new StatusReport(testBin,
                testUser,
                null,
                null,
                null,
                LocalDateTime.of(2026, 9, 9, 12, 0)
        );
        statusReportWriter.writeJson(testFile, List.of(nullReport));

        String contents = Files.readString(testFile);

        assertTrue(contents.contains("\"binID\":1"));
        assertTrue(contents.contains("\"user\":\"Test\""));
        assertTrue(contents.contains("\"userID\":1"));
        assertTrue(contents.contains("\"status\":null"));
        assertTrue(contents.contains("\"issue\":null"));
        assertTrue(contents.contains("\"description\":null"));
    }
}
