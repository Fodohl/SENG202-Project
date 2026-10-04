package seng202.team1.helpers;

import org.jooq.impl.QOM;

import java.time.*;

/**
 * Utility class to help with date objects
 */
public class DateHelpers {


    private  static final ZoneId NZT = ZoneId.of("Pacific/Auckland");

    /**
     * Convert a local datetime into a unix timestamp instant
     *
     * @param time The local time
     * @return The converted instant
     */
    public static Instant fromLocalTime(LocalDateTime time){


        return time.atZone(NZT).toInstant();
    }

    /**
     * Convert  a unix timestamp instant int an offset time
     *
     * @param time The local time
     * @return The converted instant
     */
    public static OffsetDateTime toOffsetTime(Instant time){

        if(time == null)
            return null;

        return time.atZone(NZT).toOffsetDateTime();
    }

}
