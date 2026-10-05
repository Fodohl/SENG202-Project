package seng202.team1.unittests.helpers;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seng202.team1.helpers.DateHelpers;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class DateHelpersTest extends TrashAppTest {

    TimeZone originalTimeZone = TimeZone.getDefault();

    @Test
    void dateConversionUsesDefaultTimezone() {

        // Enforce NZ time
        TimeZone.setDefault(TimeZone.getTimeZone("Pacific/Auckland"));
        ZoneId currentZone = ZoneId.systemDefault();

        // Get a instant
        LocalDateTime localDateTime = LocalDateTime.of(2026, 9, 17, 10, 0, 0);
        Instant expectedInstant = localDateTime.atZone(currentZone).toInstant();

        // Ensure conversion uses the expected zone
        Instant actualInstant = DateHelpers.fromLocalTime(localDateTime);
        assertEquals(expectedInstant, actualInstant);
    }

    @AfterEach
    void resetTimeZone() {
        TimeZone.setDefault(originalTimeZone);
    }
}