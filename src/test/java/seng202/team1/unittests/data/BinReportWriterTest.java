package seng202.team1.unittests.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.*;
import seng202.team1.models.BinReport;
import seng202.team1.models.User;

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
 */

public class BinReportWriterTest {
    private BinReportWriter binReportWriter;
    private BinReport testBinReport;
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


}
