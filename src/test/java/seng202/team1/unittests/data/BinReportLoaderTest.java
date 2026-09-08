package seng202.team1.unittests.data_managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data_managers.BinObject;
import seng202.team1.data_managers.BinReportLoader;
import seng202.team1.data_managers.DataLoader;
import seng202.team1.models.BinReport;
import seng202.team1.models.User;

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
    private BinObject testBin;
    private User testUser;

    @BeforeEach
    public void setUp() {
        binReportLoader = new BinReportLoader();
        binReportLoader.readJson();
        testBin = new BinObject(1, 1.0, 1.0, BinObject.BinType.GENERAL_WASTE);
        testUser = new User(1, "Test");
        testBinReport = new BinReport(
                testBin,
                testUser,
                BinObject.BinStatus.FULL,
                LocalDateTime.now()
        );
    }

    @Test
    public void testReadJsonPopulateLogs() {
        assertFalse(binReportLoader.getBinReports().isEmpty());
    }

    @Test
    public void testGetLogsIsNotChangeable() {
        List<BinReport> binReports = binReportLoader.getBinReports();
        assertThrows(UnsupportedOperationException.class, () -> binReports.add(testBinReport));
    }
}
