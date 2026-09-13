package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.*;
import seng202.team1.data.BinReport;
import seng202.team1.data.User;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests BinReportStore
 *
 * @author Connor Rocket
 * @author Archie Georgeson
 */
public class BinReportStoreTest {

    @TempDir
    Path tempDir;

    private BinReportStore store;
    private String testString = "binreports.json";


    @BeforeEach
    void setUp() {
        Path testFile = tempDir.resolve(testString);
        store = new BinReportStore(testFile);
    }


    @Test
    void addReport() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        BinReport report = new BinReport(
                new Bin(197, -43.605467593,
                        172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                time
        );


        store.add(report);

        List<BinReport> reports = store.getReports();

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
        BinReport report1 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(198, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(2, reports.size());

    }

    @Test
    void addTwoReportsFromDifferentUsers() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(483, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(2, reports.size());

    }

    @Test
    void addingReportPersists() throws IOException {
        LocalDateTime time = LocalDateTime.now();
        BinReport report1 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                time
        );
        store.add(report1);
        BinReportStore newStore = new BinReportStore(tempDir.resolve(testString));

        List<BinReport> reports = newStore.getReports();

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
        BinReport report1 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report3 = new BinReport(new Bin(197, -43.605467593,
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
        List<BinReport> reports = store.getReports();
        assertEquals(2, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void removeReportPersists() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report3 = new BinReport(new Bin(197, -43.605467593,
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
        BinReportStore newStore = new BinReportStore(tempDir.resolve(testString));
        List<BinReport> reports = newStore.getReports();
        assertEquals(2, reports.size());
        assertEquals(123, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void differentUsersCanReportSameBin() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(2, reports.size());
    }

    @Test
    void sameUserReportingSameBinReplacesOldReport() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.EMPTY, reports.get(0).getStatus());
        assertEquals(BinIssue.OTHER, reports.get(0).getIssue());
    }

    @Test
    void sameUserReportingSameBinPersistsIssueIfNull() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                null,
                null,
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.EMPTY, reports.get(0).getStatus());
        assertEquals(BinIssue.GRAFFITI, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
    }

    @Test
    void sameUserReportingSameBinPersistsStatusIfNull() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                null,
                BinIssue.OTHER,
                "New description",
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.FULL, reports.get(0).getStatus());
        assertEquals(BinIssue.OTHER, reports.get(0).getIssue());
        assertEquals("New description", reports.get(0).getDescription());
    }

    @Test
    void sameUserReportingSameBinPersistsIfAllNull() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.FULL,
                BinIssue.GRAFFITI,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                null,
                null,
                null,
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.FULL, reports.get(0).getStatus());
        assertEquals(BinIssue.GRAFFITI, reports.get(0).getIssue());
        assertEquals("Test description", reports.get(0).getDescription());
    }

    @Test
    void getReportsByIdReturnsCorrectSubset() throws IOException {
        BinReport report1 = new BinReport(new Bin(483, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report3 = new BinReport(new Bin(197, -43.605467593,
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
        List<BinReport> reports = store.getReportsById(197);
        assertEquals(2, reports.size());
        assertEquals(321, reports.get(0).getSubmittedBy().getUserID());
        assertEquals(231, reports.get(1).getSubmittedBy().getUserID());
    }

    @Test
    void getReportsByIdReturnsEmptyWithNoMatchingId() throws IOException {
        BinReport report1 = new BinReport(new Bin(483, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        BinReport report3 = new BinReport(new Bin(197, -43.605467593,
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
        List<BinReport> reports = store.getReportsById(1);
        assertEquals(0, reports.size());
    }

    @Test
    void addingNullReportThrowsNullPointerException() throws IOException {
        assertThrows(NullPointerException.class, () -> store.add(null));
    }

    @Test
    void getReportsListIsImmutable() throws IOException {
        BinReport report1 = new BinReport(new Bin(483, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                BinIssue.OTHER,
                "Test description",
                LocalDateTime.now()
        );

        store.add(report1);
        List<BinReport> reports = store.getReports();
        assertThrows(UnsupportedOperationException.class, () -> reports.add(null));
    }
}
