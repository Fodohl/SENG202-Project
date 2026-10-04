package seng202.team1.unittests.data.status;

import org.junit.jupiter.api.Test;
import seng202.team1.data.status.BinTrollService;
import seng202.team1.types.user.User;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.status.StatusReport;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        Instant now = Instant.now();

        assertEquals(12, BinTrollService.getTrolls(null, now));
    }

    @Test
    void testUserGetsTrollsFromReport() {
        User user = new User(10, null, null, "User1", null, null, null);
        Bin bin = mock(Bin.class);
        when(bin.getID()).thenReturn(1);

        StatusReport firstReport = mock(StatusReport.class);
        when(firstReport.getBinId()).thenReturn(bin.getID());
        when(firstReport.getUserId()).thenReturn(user.id());
        when(firstReport.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0).toInstant(ZoneOffset.UTC));

        StatusReport secondReport = mock(StatusReport.class);
        when(secondReport.getBinId()).thenReturn(bin.getID());
        when(secondReport.getUserId()).thenReturn(user.id());
        when(secondReport.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 15, 12, 0).toInstant(ZoneOffset.UTC));

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
        when(first.getBinId()).thenReturn(bin.getID());
        when(first.getUserId()).thenReturn(user2.id());
        when(first.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0).toInstant(ZoneOffset.UTC));

        StatusReport second = mock(StatusReport.class);
        when(second.getBinId()).thenReturn(bin.getID());
        when(second.getUserId()).thenReturn(user1.id());
        when(second.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 22, 12, 0).toInstant(ZoneOffset.UTC));

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
        when(bin1Report.getBinId()).thenReturn(bin1.getID());
        when(bin1Report.getUserId()).thenReturn(user.id());
        when(bin1Report.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0).toInstant(ZoneOffset.UTC));

        StatusReport bin2Report = mock(StatusReport.class);
        when(bin2Report.getBinId()).thenReturn(bin2.getID());
        when(bin2Report.getUserId()).thenReturn(user.id());
        when(bin2Report.getSubmittedAt())
                .thenReturn(LocalDateTime.of(2026, 1, 2, 12, 0).toInstant(ZoneOffset.UTC));

        int trolls = BinTrollService.getTrollsSlain(
                user,
                List.of(bin1Report, bin2Report)
        );

        assertEquals(24, trolls);
    }
}


