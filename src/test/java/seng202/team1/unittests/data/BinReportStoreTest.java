package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.bin.BinIssue;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.User;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.data.bin.BinStatus;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests StatusReportStore
 *
 * @author Connor Rocket
 * @author Archie Georgeson
 */
public class StatusReportStoreTest {

    @TempDir
    Path tempDir;

    private StatusReportStore store;
    private String testString = "binreports.json";


    @BeforeEach
    void setUp() {
        Path testFile = tempDir.resolve(testString);
        store = new StatusReportStore(testFile);
    }


    @Test
    void addReport() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        StatusReport report = new StatusReport(
                new Bin(197, -43.605467593,
                        172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                time
        );


        store.add(report);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(197, reports.get(0).getBinID());
        assertEquals("Test1", reports.get(0).getSubmittedBy().getName());
        assertEquals(BinStatus.EMPTY, reports.get(0).getStatus());
        assertEquals(BinIssue.OTHER, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
        assertEquals(time, reports.get(0).getSubmittedAt());
    }

    @Test
    void addTwoReportsToDifferentBins() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(198, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
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
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(483, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
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
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                time
        );
        store.add(report1);
        StatusReportStore newStore = new StatusReportStore(tempDir.resolve(testString));

        List<StatusReport> reports = newStore.getReports();

        assertEquals(1, reports.size());
        assertEquals(197, reports.get(0).getBinID());
        assertEquals("Test1", reports.get(0).getSubmittedBy().getName());
        assertEquals(BinStatus.EMPTY, reports.get(0).getStatus());
        assertEquals(BinIssue.OTHER, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
        assertEquals(time, reports.get(0).getSubmittedAt());

    }

    @Test
    void removeReportRemovesCorrectReport() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(231, "Test3"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
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
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(231, "Test3"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);
        store.add(report3);
        store.removeReport(report2);
        StatusReportStore newStore = new StatusReportStore(tempDir.resolve(testString));
        List<StatusReport> reports = newStore.getReports();
        assertEquals(2, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void differentUsersCanReportSameBin() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(2, reports.size());
    }

    @Test
    void sameUserReportingSameBinReplacesOldReport() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.EMPTY, reports.get(0).getStatus());
        assertEquals(BinIssue.OTHER, reports.get(0).getIssue());
    }

    @Test
    void sameUserReportingSameBinPersistsIssueIfNull() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                null,
                null,
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.EMPTY, reports.get(0).getStatus());
        assertEquals(BinIssue.GRAFFITI, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
    }

    @Test
    void sameUserReportingSameBinPersistsStatusIfNull() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                null,
                BinIssue.OTHER,
                "New description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.FULL, reports.get(0).getStatus());
        assertEquals(BinIssue.OTHER, reports.get(0).getIssue());
        assertEquals("New description", reports.get(0).getDescription());
    }

    @Test
    void sameUserReportingSameBinPersistsIfAllNull() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                null,
                null,
                null,
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<StatusReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.FULL, reports.get(0).getStatus());
        assertEquals(BinIssue.GRAFFITI, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
    }

    @Test
    void getReportsByIdReturnsCorrectSubset() throws IOException {
        StatusReport report1 = new StatusReport(new Bin(483, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(231, "Test3"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
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
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report2 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        StatusReport report3 = new StatusReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(231, "Test3"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
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
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        List<StatusReport> reports = store.getReports();
        assertThrows(UnsupportedOperationException.class, () -> reports.add(null));
    }
}
