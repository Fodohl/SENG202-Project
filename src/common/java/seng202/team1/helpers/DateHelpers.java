package seng202.team1.common;

import org.jooq.impl.QOM;

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

    /**
     * Convert  a unix timestamp instant int an offset time
     *
     * @param time The local time
     * @return The converted instant
     */
    public static OffsetDateTime toOffsetTime(Instant time){

        ZoneId timezone = ZoneOffset.systemDefault();

        return time.atZone(timezone).toOffsetDateTime();
    }

}
