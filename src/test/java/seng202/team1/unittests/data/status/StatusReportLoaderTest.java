package seng202.team1.unittests.data.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;

import seng202.team1.types.User;
import seng202.team1.data.status.StatusReportLoader;
import seng202.team1.unittests.TrashAppTest;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests StatusReportLoader
 *
 * @author Archie Georgeson
 */
public class StatusReportLoaderTest extends TrashAppTest {
    private StatusReportLoader statusReportLoader;
    private StatusReport testStatusReport;
    private Bin testBin;
    private User testUser;

    @TempDir
    public Path temporaryDirectory;

    @BeforeEach
    public void setUp() {
        statusReportLoader = new StatusReportLoader(bins);
        statusReportLoader.readJson();
        testBin = new Bin(1, 1.0, 1.0, BinCategory.GENERAL_WASTE, Instant.now(), "testPark");
        testUser = new User(1, Instant.now(), Instant.now(), "Test", "", "", 0);
        testStatusReport = new StatusReport(
                testBin,
                testUser,
                BinFullness.FULL,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );
    }

    @Test
    public void readJsonPopulateLogs() {
        assertFalse(statusReportLoader.getStatusReports().isEmpty());
    }

    @Test
    public void getReportsIsNotChangeable() {
        List<StatusReport> statusReports = statusReportLoader.getStatusReports();
        assertThrows(UnsupportedOperationException.class, () -> statusReports.add(testStatusReport));
    }

    @Test
    public void reportMapsCorrectly() {
        List<StatusReport> statusReports = statusReportLoader.getStatusReportsById(1);
        assertEquals(1, statusReports.get(1).getBinID());
        assertEquals("Test2", statusReports.get(1).getSubmittedBy().name());
        assertEquals(321, statusReports.get(1).getSubmittedBy().id());
        assertEquals(BinFullness.EMPTY, statusReports.get(1).getFullness());
        assertEquals(ReportType.MISSING, statusReports.get(1).getIssue());
        assertEquals("Test description", statusReports.get(1).getDescription());
    }

    @Test
    public void nullMapsCorrectly() {
        List<StatusReport> statusReports = statusReportLoader.getStatusReportsById(1);
        assertEquals(1, statusReports.get(0).getBinID());
        assertEquals("Test1", statusReports.get(0).getSubmittedBy().name());
        assertEquals(123, statusReports.get(0).getSubmittedBy().id());
        assertNull(statusReports.get(0).getFullness());
        assertNull(statusReports.get(0).getIssue());
        assertNull(statusReports.get(0).getDescription());
    }

    @Test
    public void getStatusReportsByIdReturnsCorrectBinSubset() {
        List<StatusReport> statusReports = statusReportLoader.getStatusReportsById(1);
        assertEquals(2, statusReports.size());
        assertEquals(123, statusReports.get(0).getSubmittedBy().id());
        assertEquals(321, statusReports.get(1).getSubmittedBy().id());
    }

    @Test
    public void getStatusReportsByIdReturnsEmptyForNoMatchingId() {
        List<StatusReport> statusReports = statusReportLoader.getStatusReportsById(234);
        assertEquals(0, statusReports.size());
    }

    @Test
    public void readJsonNonExistentFileReturnsEmpty() {
        Path missingPath = temporaryDirectory.resolve("does-not-exist.json");
        statusReportLoader = new StatusReportLoader(bins, missingPath);
        assertTrue(statusReportLoader.getStatusReports().isEmpty());
    }

    @Test
    public void readJsonCalledTwiceReplacesOldStatusReports() {
        statusReportLoader.readJson();
        statusReportLoader.readJson();
        List<StatusReport> statusReports = statusReportLoader.getStatusReports();
        assertEquals(3, statusReports.size());
    }
}
