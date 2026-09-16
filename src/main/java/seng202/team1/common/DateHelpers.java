package seng202.team1.common;

import java.time.*;

public class DateHelpers {


    public static Instant fromLocalTime(LocalDateTime time){

        ZoneId timezone = ZoneOffset.systemDefault();

        return time.atZone(timezone).toInstant();
    }

}
