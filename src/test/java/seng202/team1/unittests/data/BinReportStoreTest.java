package seng202.team1.unittests.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seng202.team1.data.*;
import seng202.team1.models.BinReport;
import seng202.team1.models.User;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests BinReportStore
 *
 * @author Connor Rocket
 */
public class BinReportStoreTest {

    @TempDir
    Path tempDir;

    private BinReportStore store;
    private Path testFile;

    @BeforeEach
    public void setUp() {
        this.store = new BinReportStore();
    }

    @Test
    void addReport() throws IOException {
        BinReport report = new BinReport(
                new Bin(197, -43.605467593,
                        172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                LocalDateTime.now()
        );


        store.add(report);

        List<BinReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(1, reports.get(0).getBinID());
        assertEquals("Connor", reports.get(0).getSubmittedBy().getName());
        assertEquals(BinStatus.FULL, reports.get(0).getDescription());
    }

    @Test
    void addTwoReports() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(2, reports.size());

        }

    @Test
    void differentUsersCanReportSameBin() throws IOException {
        BinReport report1 = new BinReport(new Bin(197, 43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(123, "Test1"),
                BinStatus.EMPTY,
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
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
                BinStatus.EMPTY,
                LocalDateTime.now()
        );

        BinReport report2 = new BinReport(new Bin(197, -43.605467593,
                172.693149814, TrashCategory.RECYCLABLE),
                new User(321, "Test2"),
                BinStatus.EMPTY,
                LocalDateTime.now()
        );

        store.add(report1);
        store.add(report2);

        List<BinReport> reports = store.getReports();

        assertEquals(1, reports.size());
        assertEquals(BinStatus.FULL,
                reports.get(0).getDescription());
    }
}
