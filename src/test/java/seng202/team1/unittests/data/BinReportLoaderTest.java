package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.bin.BinIssue;
import seng202.team1.data.bin.BinReport;
import seng202.team1.data.User;
import seng202.team1.data.bin.BinReportLoader;
import seng202.team1.data.bin.BinStatus;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests BinReportLoader
 *
 * @author Archie Georgeson
 */
public class BinReportLoaderTest {
    private BinReportLoader binReportLoader;
    private BinReport testBinReport;
    private Bin testBin;
    private User testUser;

    @TempDir
    public Path temporaryDirectory;

    @BeforeEach
    public void setUp() {
        binReportLoader = new BinReportLoader();
        binReportLoader.readJson();
        testBin = new Bin(1, 1.0, 1.0, TrashCategory.GENERAL_WASTE);
        testUser = new User(1, "Test");
        testBinReport = new BinReport(
                testBin,
                testUser,
                BinStatus.FULL,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );
    }

    @Test
    public void readJsonPopulateLogs() {
        assertFalse(binReportLoader.getBinReports().isEmpty());
    }

    @Test
    public void getReportsIsNotChangeable() {
        List<BinReport> binReports = binReportLoader.getBinReports();
        assertThrows(UnsupportedOperationException.class, () -> binReports.add(testBinReport));
    }

    @Test
    public void reportMapsCorrectly() {
        List<BinReport> binReports = binReportLoader.getBinReportsById(197);
        assertEquals(197, binReports.get(1).getBinID());
        assertEquals("Test2", binReports.get(1).getSubmittedBy().getName());
        assertEquals(321, binReports.get(1).getSubmittedBy().getUserID());
        assertEquals(BinStatus.EMPTY, binReports.get(1).getStatus());
        assertEquals(BinIssue.MISSING, binReports.get(1).getIssue());
        assertEquals("Test description", binReports.get(1).getDescription());
    }

    @Test
    public void nullMapsCorrectly() {
        List<BinReport> binReports = binReportLoader.getBinReportsById(197);
        assertEquals(197, binReports.get(0).getBinID());
        assertEquals("Test1", binReports.get(0).getSubmittedBy().getName());
        assertEquals(123, binReports.get(0).getSubmittedBy().getUserID());
        assertNull(binReports.get(0).getStatus());
        assertNull(binReports.get(0).getIssue());
        assertNull(binReports.get(0).getDescription());
    }

    @Test
    public void getBinReportsByIdReturnsCorrectBinSubset() {
        List<BinReport> binReports = binReportLoader.getBinReportsById(197);
        assertEquals(2, binReports.size());
        assertEquals(123, binReports.get(0).getSubmittedBy().getUserID());
        assertEquals(321, binReports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    public void getBinReportsByIdReturnsEmptyForNoMatchingId() {
        List<BinReport> binReports = binReportLoader.getBinReportsById(1);
        assertEquals(0, binReports.size());
    }

    @Test
    public void readJsonNonExistentFileReturnsEmpty() {
        Path missingPath = temporaryDirectory.resolve("does-not-exist.json");
        binReportLoader = new BinReportLoader(missingPath);
        assertTrue(binReportLoader.getBinReports().isEmpty());
    }

    @Test
    public void readJsonCalledTwiceReplacesOldBinReports() {
        binReportLoader.readJson();
        binReportLoader.readJson();
        List<BinReport> binReports = binReportLoader.getBinReports();
        assertEquals(3, binReports.size());
    }
}
