/** package seng202.team1.data.status;

import seng202.team1.api.trashdb.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class StatusReportScoring {

    public static int calculatePoints(
            LocalDateTime previousReport,
            LocalDateTime newReport) {
        if (previousReport == null) {
            return 40;
        }

        long days = Duration.between(previousReport, newReport).toDays();

        if (days >= 30) {
            return 30;
        } else if (days >= 14) {
            return 20;
        } else if (days >= 7) {
            return 10;
        } else {
            return 5;
        }

    }

    public static int totalUserPoints(
            User user,
            List<StatusReport> report) {

    }
}
*/