package seng202.team1.data.status;

import seng202.team1.types.user.User;
import seng202.team1.types.status.StatusReport;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Objects;

/**
 * Sets the number of trolls in a bin and gets how many slain over a users use of the app
 * @author Connor Rocket
 */

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

    public static int getTrollsSlain(
            User user,
            List<StatusReport> reports) {

        List<StatusReport> sortedReports = reports.stream()
                .sorted(Comparator.comparing(StatusReport::getSubmittedAt))
                .toList();

        Map<Integer, LocalDateTime> lastReportTimes = new HashMap<>();

        int totalTrolls = 0;

        for (StatusReport report : sortedReports) {

            int binID = report.getBin().getID();

            LocalDateTime previousReport =
                    lastReportTimes.get(binID);

            int trollsAtBin = getTrolls(
                    previousReport,
                    report.getSubmittedAt());

            if (Objects.equals(report.getSubmittedBy().id(), user.id())) {
                totalTrolls += trollsAtBin;
            }

            lastReportTimes.put(binID, report.getSubmittedAt());

        }
        return totalTrolls;
    }
}


