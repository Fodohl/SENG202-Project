package seng202.team1.data.status;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BinTrollService {
    public static int getTrolls(
            LocalDateTime lastReport,
            LocalDateTime currentReport
    ) {
        if (lastReport == null) {
            return 12;
        }

        long days = ChronoUnit.DAYS.between(lastReport, currentReport);

        if (days >= 30) {
            return 12;
        } else if (days >= 21) {
            return 8;
        } else if (days >= 14) {
            return 4;
        } else if (days >= 7) {
            return 2;
        }

        return 0;
    }
}

