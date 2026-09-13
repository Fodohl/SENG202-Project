package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.bin.BinIssue;
import seng202.team1.data.bin.BinReport;
import seng202.team1.data.User;
import seng202.team1.data.bin.BinReportWriter;
import seng202.team1.data.bin.BinStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests BinReportWriter
 *
 * @author connorrocket
 * @author Archie Georgeson
 */

public class BinReportWriterTest {
    private BinReportWriter binReportWriter;
    private BinReport testBinReport;
    private BinReport testBinReport2;
    private Bin testBin;
    private User testUser;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        binReportWriter = new BinReportWriter();

        testBin = new Bin(
                1,
                1.0,
                1.0,
                TrashCategory.GENERAL_WASTE
        );

        testUser = new User(1, "Test");

        testBinReport = new BinReport(
                testBin,
                testUser,
                BinStatus.FULL,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.of(2026, 9, 9, 12, 0)
        );

         testBinReport2 = new BinReport(
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

        binReportWriter.writeJson(testFile, List.of(testBinReport));

        assertTrue(Files.exists(testFile));
    }

    @Test
    public void testWriteJsonContainsReport() throws IOException {
        Path testFile = tempDir.resolve("binreports.json");

        binReportWriter.writeJson(testFile, List.of(testBinReport));

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
        binReportWriter.writeJson(testFile, List.of());
        assertTrue(Files.exists(testFile));
        String contents = Files.readString(testFile);
        assertTrue(contents.contains("\"binReports\":[]"));
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
        binReportWriter.writeJson(testFile, List.of(testBinReport, testBinReport2));

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
        binReportWriter.writeJson(testFile, List.of(testBinReport));
        binReportWriter.writeJson(testFile, List.of(testBinReport2));
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
        BinReport nullReport = new BinReport(testBin,
                testUser,
                null,
                null,
                null,
                LocalDateTime.of(2026, 9, 9, 12, 0)
        );
        binReportWriter.writeJson(testFile, List.of(nullReport));

        String contents = Files.readString(testFile);

        assertTrue(contents.contains("\"binID\":1"));
        assertTrue(contents.contains("\"user\":\"Test\""));
        assertTrue(contents.contains("\"userID\":1"));
        assertTrue(contents.contains("\"status\":null"));
        assertTrue(contents.contains("\"issue\":null"));
        assertTrue(contents.contains("\"description\":null"));
    }
}
