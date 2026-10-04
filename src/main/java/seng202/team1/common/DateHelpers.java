package seng202.team1.common;

import java.time.*;

/**
 * Utility class to help with date objects
 */
public class DateHelpers {


    /**
     * Convert a local datetime into a unix timestamp instant
     *
     * @param time The local time
     * @return The converted instant
     */
    public static Instant fromLocalTime(LocalDateTime time){

        ZoneId timezone = ZoneOffset.systemDefault();

        return time.atZone(timezone).toInstant();
    }

}
