package seng202.team1.unittests.database.DAO;

import org.junit.jupiter.api.Test;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StatusReportDAOTest extends TrashAppTest {
    private static final int DEFAULT_BIN_ID = 1;
    private static final int DEFAULT_USER_ID = 1;

    private StatusReport makeReport() {
        return makeReport(DEFAULT_BIN_ID, DEFAULT_USER_ID, ReportType.OTHER);
    }

    private StatusReport makeReport(int binId, int userId, ReportType issue) {
        return new StatusReport(
                1,
                binId,
                userId,
                BinFullness.UNKNOWN,
                issue,
                "test description",
                Instant.now().truncatedTo(ChronoUnit.MILLIS),
                null
        );
    }

    private void assertReportsEqual(StatusReport expected, StatusReport actual) {
        assertEquals(expected.getBinId(), actual.getBinId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getFullness(), actual.getFullness());
        assertEquals(expected.getIssue(), actual.getIssue());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getSubmittedAt().truncatedTo(ChronoUnit.MILLIS),
                actual.getSubmittedAt());
    }

    @Test
    void testAddStatusReport() {
        StatusReport report = makeReport();
        statusReportDAO.add(report);
        assertEquals(1, statusReportDAO.getAll().size());
        assertReportsEqual(report, statusReportDAO.getAll().getFirst());
    }

    @Test
    void testGetOne() {
        StatusReport report = makeReport();
        StatusReport report2 = makeReport(1, 1, ReportType._FULLNESS_UPDATE);
        statusReportDAO.add(report);
        statusReportDAO.add(report2);
        assertEquals(2, statusReportDAO.getAll().size());
        assertReportsEqual(report2, statusReportDAO.getOne(2));
        assertReportsEqual(report, statusReportDAO.getOne(1));
    }

    @Test
    void testGetOneNonExistentId() {
        StatusReport report = makeReport();
        statusReportDAO.add(report);
        assertNull(statusReportDAO.getOne(-1));
    }

    @Test
    void testGetAllEmpty() {
        assertEquals(0, statusReportDAO.getAll().size());
    }

    @Test
    void testGetAllReturnsAll() {
        StatusReport report = makeReport();
        StatusReport report2 = makeReport(1, 2, ReportType._FULLNESS_UPDATE);
        StatusReport report3 = makeReport(1, 3, ReportType.GRAFFITI);
        statusReportDAO.add(report);
        statusReportDAO.add(report2);
        statusReportDAO.add(report3);
        assertReportsEqual(report, statusReportDAO.getAll().get(0));
        assertReportsEqual(report2, statusReportDAO.getAll().get(1));
        assertReportsEqual(report3, statusReportDAO.getAll().get(2));
    }

    @Test
    void testGetReportsByBinId() {
        StatusReport report = makeReport();
        StatusReport report2 = makeReport(2, 2, ReportType._FULLNESS_UPDATE);
        StatusReport report3 = makeReport(3, 3, ReportType.GRAFFITI);
        statusReportDAO.add(report);
        statusReportDAO.add(report2);
        statusReportDAO.add(report3);
        assertReportsEqual(report2, statusReportDAO.getReportsByBinId(2).getFirst());
    }

    @Test
    void testGetReportsByBinIdNoneFound() {
        StatusReport report = makeReport();
        statusReportDAO.add(report);
        assertEquals(0, statusReportDAO.getReportsByBinId(-1).size());
    }

    @Test
    void testGetReportsByUserId() {
        StatusReport report = makeReport();
        StatusReport report2 = makeReport(2, 2, ReportType._FULLNESS_UPDATE);
        StatusReport report3 = makeReport(3, 3, ReportType.GRAFFITI);
        statusReportDAO.add(report);
        statusReportDAO.add(report2);
        statusReportDAO.add(report3);
        assertReportsEqual(report2, statusReportDAO.getReportsByUserId(2).getFirst());
    }

    @Test
    void testGetReportsByUserIdNoneFound() {
        StatusReport report = makeReport();
        statusReportDAO.add(report);
        assertEquals(0, statusReportDAO.getReportsByUserId(-1).size());
    }

    @Test
    void testGetFullnessReportsOnlyReturnsFullnessUpdates() {
        StatusReport report = makeReport();
        StatusReport fullnessReport = makeReport(1,1,ReportType._FULLNESS_UPDATE);
        statusReportDAO.add(report);
        statusReportDAO.add(report);
        statusReportDAO.add(fullnessReport);
        statusReportDAO.add(report);
        assertEquals(1, statusReportDAO.getFullnessReportsByBinId(1).size());
        assertReportsEqual(fullnessReport, statusReportDAO.getFullnessReportsByBinId(1).getFirst());
    }

    @Test
    void testGetFullnessReportsReturnsCorrectBinId() {
        StatusReport fullnessReport = makeReport(1,1,ReportType._FULLNESS_UPDATE);
        StatusReport fullnessReport2 = makeReport(2,1,ReportType._FULLNESS_UPDATE);
        StatusReport fullnessReport3 = makeReport(3,1,ReportType._FULLNESS_UPDATE);
        statusReportDAO.add(fullnessReport);
        statusReportDAO.add(fullnessReport2);
        statusReportDAO.add(fullnessReport3);
        assertEquals(1, statusReportDAO.getFullnessReportsByBinId(2).size());
        assertReportsEqual(fullnessReport2,  statusReportDAO.getFullnessReportsByBinId(2).getFirst());
    }

    @Test
    void testGetFullnessReportsNoneFound() {
        StatusReport fullnessReport = makeReport();
        statusReportDAO.add(fullnessReport);
        assertEquals(0, statusReportDAO.getFullnessReportsByBinId(-1).size());
    }

    @Test
    void testUpdateStatusReport() {
        StatusReport report = makeReport();
        StatusReport updatedReport = makeReport(1, 1, ReportType._FULLNESS_UPDATE);
        statusReportDAO.add(report);
        statusReportDAO.update(updatedReport);
        assertEquals(1,  statusReportDAO.getAll().size());
        assertReportsEqual(updatedReport, statusReportDAO.getAll().get(0));
    }

    @Test
    void testUpdateStatusReportNoneFound() {
        StatusReport report = makeReport();
        StatusReport falseReport = new StatusReport(-1, 1, 1, BinFullness.EMPTY, ReportType._FULLNESS_UPDATE, "Test Description 2", Instant.now(), null);
        statusReportDAO.add(report);
        statusReportDAO.update(falseReport);
        assertEquals(1, statusReportDAO.getAll().size());
        assertReportsEqual(report, statusReportDAO.getAll().get(0));
    }

    @Test
    void testUpdateDoesNotAffectOtherReports() {
        StatusReport report = makeReport();
        StatusReport updatedReport = makeReport(1, 1, ReportType._FULLNESS_UPDATE);
        StatusReport otherReport = makeReport(2, 2, ReportType._FULLNESS_UPDATE);
        statusReportDAO.add(report);
        statusReportDAO.add(otherReport);
        statusReportDAO.update(updatedReport);
        assertEquals(2, statusReportDAO.getAll().size());
        assertReportsEqual(updatedReport, statusReportDAO.getAll().get(0));
        assertReportsEqual(otherReport, statusReportDAO.getAll().get(1));
    }

    @Test
    void testDeleteStatusReport() {
        StatusReport report = makeReport();
        statusReportDAO.add(report);
        statusReportDAO.delete(1);
        assertEquals(0, statusReportDAO.getAll().size());
        assertNull(statusReportDAO.getOne(1));
    }

    @Test
    void testDeleteStatusReportNoneFound() {
        StatusReport report = makeReport();
        statusReportDAO.add(report);
        statusReportDAO.delete(-1);
        assertEquals(1, statusReportDAO.getAll().size());
    }

    @Test
    void testDeleteDoesNotAffectOtherReports() {
        StatusReport report = makeReport();
        StatusReport otherReport = makeReport(2, 2, ReportType._FULLNESS_UPDATE);
        StatusReport otherReport2 = makeReport(3, 3, ReportType._FULLNESS_UPDATE);
        statusReportDAO.add(otherReport);
        statusReportDAO.add(report);
        statusReportDAO.add(otherReport2);
        statusReportDAO.delete(2);
        assertEquals(2, statusReportDAO.getAll().size());
        assertReportsEqual(otherReport, statusReportDAO.getAll().get(0));
        assertReportsEqual(otherReport2, statusReportDAO.getAll().get(1));
    }
}
