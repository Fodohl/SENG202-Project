package seng202.team1.unittests.data.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;

import seng202.team1.types.User;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.types.bin.BinCategory;
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
        store = new StatusReportStore(bins, testFile);
    }


    @Test
    void addReport() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        StatusReport report = new StatusReport(
                new seng202.team1.types.bin.Bin(1, -43.605467593,
                        172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                time
        );


        store.add(report);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(1, reports.get(0).getBinID());
        assertEquals("Test", reports.get(0).getSubmittedBy().name());
        assertEquals(BinFullness.EMPTY, reports.get(0).getFullness());
        assertEquals(ReportType.OTHER, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
        assertEquals(time, reports.get(0).getSubmittedAt());
    }

    @Test
    void addTwoReportsToDifferentBins() throws IOException {
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(198, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
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
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        bins.add(new seng202.team1.types.bin.Bin(2, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), null, null, BinFullness.EMPTY));
        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(2, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, Instant.now(), Instant.now(), "Test", "", "", 0),
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
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                time
        );
        store.add(report1);
        StatusReportStore newStore = new StatusReportStore(bins, tempDir.resolve(testString));

        List<StatusReport> reports = newStore.getReports();

        assertEquals(1, reports.size());
        assertEquals(1, reports.get(0).getBinID());
        assertEquals("Test", reports.get(0).getSubmittedBy().name());
        assertEquals(BinFullness.EMPTY, reports.get(0).getFullness());
        assertEquals(ReportType.OTHER, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
        assertEquals(time, reports.get(0).getSubmittedAt());

    }

    @Test
    void removeReportRemovesCorrectReport() throws IOException {
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, Instant.now(), Instant.now(), "Test", "", "", 0),
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
        assertEquals(3, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().id());
        assertEquals(321, reports.get(1).getSubmittedBy().id());
    }

    @Test
    void removeReportPersists() throws IOException {
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);
        store.add(report3);
        store.removeReport(report2);
        StatusReportStore newStore = new StatusReportStore(bins, tempDir.resolve(testString));
        List<StatusReport> reports = newStore.getReports();
        assertEquals(3, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().id());
        assertEquals(321, reports.get(1).getSubmittedBy().id());
    }

    @Test
    void differentUsersCanReportSameBin() throws IOException {
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, 43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, Instant.now(), Instant.now(), "Test", "", "", 0),
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

        bins.add(new seng202.team1.types.bin.Bin(2, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), null, null, BinFullness.EMPTY));
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(2, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);
        store.add(report3);
        List<StatusReport> reports = store.getReportsById(1);
        assertEquals(2, reports.size());
        assertEquals(321, reports.get(0).getSubmittedBy().id());
        assertEquals(231, reports.get(1).getSubmittedBy().id());
    }

    @Test
    void getReportsByIdReturnsEmptyWithNoMatchingId() throws IOException {

        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(321, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new seng202.team1.types.bin.Bin(1, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(231, Instant.now(), Instant.now(), "Test", "", "", 0),
                BinFullness.EMPTY,
                ReportType.OTHER,
                "Test description",
                LocalDateTime.now()
        );
        store.add(report1);
        store.add(report2);
        store.add(report3);
        List<StatusReport> reports = store.getReportsById(2);
        assertEquals(0, reports.size());
    }

    @Test
    void addingNullReportThrowsNullPointerException() throws IOException {
        assertThrows(NullPointerException.class, () -> store.add(null));
    }

    @Test
    void getReportsListIsImmutable() throws IOException {

        bins.add(new seng202.team1.types.bin.Bin(2, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), null, null, BinFullness.EMPTY));
        StatusReport report1 = new StatusReport(new seng202.team1.types.bin.Bin(2, -43.605467593,
                172.693149814, BinCategory.RECYCLABLE, Instant.now(), "testPark"),
                new User(123, Instant.now(), Instant.now(), "Test", "", "", 0),
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
