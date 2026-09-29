package seng202.team1.unittests.data.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.bin.*;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.User;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests StatusReportStore
 *
 * @author Connor Rocket
 * @author Archie Georgeson
 */
public class StatusReportStoreTest extends TrashAppTest {

    @TempDir
    Path tempDir;

    private StatusReportStore store;
    private String testString = "data/example_reports.json";


    @BeforeEach
    void setUp() {
        Path testFile = tempDir.resolve(testString);
        store = new StatusReportStore(binLoader, testFile);
    }


    @Test
    void addReport() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        StatusReport report = new StatusReport(
                new Bin(197, -43.605467593,
                        172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                time
        );


        store.add(report);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(197, reports.get(0).getBinID());
        assertEquals("Test1", reports.get(0).getSubmittedBy().getName());
        assertEquals(BinFullness.EMPTY, reports.get(0).getFullness());
        assertEquals(ReportType.OTHER, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
        assertEquals(time, reports.get(0).getSubmittedAt());
    }

    @Test
    void addTwoReportsToDifferentBins() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(198, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(2, reports.size());

    }

    @Test
    void addTwoReportsFromDifferentUsers() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(483, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, "Test2"),
                BinFullness.FULL,
                ReportType.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(2, reports.size());

    }

    @Test
    void addingReportPersists() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                time
        );
        store.add(report1);
        StatusReportStore newStore = new StatusReportStore(binLoader, tempDir.resolve(testString));

        List<StatusReport> reports = newStore.getReports();

        assertEquals(1, reports.size());
        assertEquals(197, reports.get(0).getBinID());
        assertEquals("Test1", reports.get(0).getSubmittedBy().getName());
        assertEquals(BinFullness.EMPTY, reports.get(0).getFullness());
        assertEquals(ReportType.OTHER, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
        assertEquals(time, reports.get(0).getSubmittedAt());

    }

    @Test
    void removeReportRemovesCorrectReport() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, "Test2"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, "Test3"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);
        store.add(report3);
        store.removeReport(report2);
        List<StatusReport> reports = store.getReports();
        assertEquals(2, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void removeReportPersists() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, "Test2"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, "Test3"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);
        store.add(report3);
        store.removeReport(report2);
        StatusReportStore newStore = new StatusReportStore(binLoader, tempDir.resolve(testString));
        List<StatusReport> reports = newStore.getReports();
        assertEquals(2, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void differentUsersCanReportSameBin() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, 43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, "Test2"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(2, reports.size());
    }

    @Test
    void getReportsByIdReturnsCorrectSubset() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(483, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, "Test2"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, "Test3"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);
        store.add(report3);
        List<StatusReport> reports = store.getReportsById(197);
        assertEquals(2, reports.size());
        assertEquals(321, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void getReportsByIdReturnsEmptyWithNoMatchingId() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(483, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, "Test2"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, "Test3"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );
        store.add(report1);
        store.add(report2);
        store.add(report3);
        List<StatusReport> reports = store.getReportsById(1);
        assertEquals(0, reports.size());
    }

    @Test
    void addingNullReportThrowsNullPointerException() throws IOException {
        assertThrows(NullPointerException.class, () -> store.add(null));
    }

    @Test
    void getReportsListIsImmutable() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(483, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, "Test1"),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        List<StatusReport> reports = store.getReports();
        assertThrows(UnsupportedOperationException.class, () -> reports.add(null));
    }
}
