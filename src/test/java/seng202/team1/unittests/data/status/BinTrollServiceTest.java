package seng202.team1.unittests.data.status;

import org.junit.jupiter.api.Test;
import seng202.team1.data.status.BinTrollService;
import seng202.team1.types.user.User;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.status.StatusReport;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static  org.mockito.Mockito.*;

/**
 * Tests BinTrollService
 * @author Connor Rocket
 */

public class BinTrollServiceTest {

    @Test
    void testNeverReported() {
        LocalDateTime now = LocalDateTime.now();

        assertEquals(12, BinTrollService.getTrolls(null, now));
    }

    @Test
    void testUserGetsTrollsFromReport() {
        User user = new User(10, null, null, "User1", null, null, null);
        Bin bin = mock(Bin.class);
        when(bin.getID()).thenReturn(1);

        StatusReport firstReport = mock(StatusReport.class);
        when(firstReport.getBin()).thenReturn(bin);
        when(firstReport.getSubmittedBy()).thenReturn(user);
        when(firstReport.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0));

        StatusReport secondReport = mock(StatusReport.class);
        when(secondReport.getBin()).thenReturn(bin);
        when(secondReport.getSubmittedBy()).thenReturn(user);
        when(secondReport.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 15, 12, 0));

        int trolls = BinTrollService.getTrollsSlain(
                user,
                List.of(firstReport, secondReport)
        );

        assertEquals(16, trolls);
    }

    @Test
    void testTrollsAwardedToCorrectUser() {

        User user1 = new User(10, null, null, "User1", null, null, null);
        User user2 = new User(20, null, null, "User2", null, null, null);

        Bin bin = mock(Bin.class);
        when(bin.getID()).thenReturn(1);

        StatusReport first = mock(StatusReport.class);
        when(first.getBin()).thenReturn(bin);
        when(first.getSubmittedBy()).thenReturn(user2);
        when(first.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0));

        StatusReport second = mock(StatusReport.class);
        when(second.getBin()).thenReturn(bin);
        when(second.getSubmittedBy()).thenReturn(user1);
        when(second.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 22, 12, 0));

        int connorTrolls = BinTrollService.getTrollsSlain(
                user1,
                List.of(first, second)
        );

        assertEquals(8, connorTrolls);
    }

    @Test
    void testBinsHaveSeparateTrollTimers() {

        User user = new User(10, null, null, "User1", null, null, null);

        Bin bin1 = mock(Bin.class);
        when(bin1.getID()).thenReturn(1);

        Bin bin2 = mock(Bin.class);
        when(bin2.getID()).thenReturn(2);

        StatusReport bin1Report = mock(StatusReport.class);
        when(bin1Report.getBin()).thenReturn(bin1);
        when(bin1Report.getSubmittedBy()).thenReturn(user);
        when(bin1Report.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0));

        StatusReport bin2Report = mock(StatusReport.class);
        when(bin2Report.getBin()).thenReturn(bin2);
        when(bin2Report.getSubmittedBy()).thenReturn(user);
        when(bin2Report.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 2, 12, 0));

        int trolls = BinTrollService.getTrollsSlain(
                user,
                List.of(bin1Report, bin2Report)
        );

        assertEquals(24, trolls);
    }
}


