package seng202.team1.unittests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.data_managers.BinObject;
import seng202.team1.models.BinReport;
import seng202.team1.models.User;
import seng202.team1.services.ReportService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ReportService implementation
 */
public class ReportServiceTest {

    private ReportService testReportService;
    private BinObject testBin;
    private User testUser;


    /**
     * Initializes a ReportService and BinObject before each test
     */
    @BeforeEach
    public void setUpTest() {
        testReportService = new ReportService();
        testBin = new BinObject(0, 0, 0, BinObject.BinType.GENERAL_WASTE);
        testUser = new User(1, "Test");
    }

    /**
     * Tests that reportService returns false when attempting to submit a report with null bin
     */
    @Test
    public void submitReportWithNullBin() {
        BinReport binReport = new BinReport(null, testUser, BinObject.BinStatus.FULL);
        assertFalse(testReportService.submitReport(binReport));
    }

    /**
     * Tests that reportService returns false when attempting to submit a report with null status
     */
    @Test
    public void submitReportWithNullDescription() {
        BinReport binReport = new BinReport(testBin, testUser, null);
        assertFalse(testReportService.submitReport(binReport));
    }

    /**
     * Tests that reportService returns true when submitting valid report and changes BinObject's BinStatus to
     * the reported BinStatus
     */
    @Test
    public void submitReportSuccess() {
        BinReport binReport = new BinReport(testBin, testUser, BinObject.BinStatus.FULL);
        assertTrue(testReportService.submitReport(binReport));
        assertEquals(BinObject.BinStatus.FULL, testBin.getBinStatus());
    }

    /**
     * Tests that reporting an incorrect location increments the incorrect location value on a BinObject
     */
    @Test
    public void submitLocationIncorrectReport() {
        BinReport binReport = new BinReport(testBin, testUser, BinObject.BinStatus.INCORRECT_LOCATION);
        assertTrue(testReportService.submitReport(binReport));
        assertEquals(1, testBin.getNumIncorrectLocations());
    }
}
