package seng202.team1.data.status;

import seng202.team1.data.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            int binID = report.getBinID();

            LocalDateTime previousReport =
                    lastReportTimes.get(binID);

            int trollsAtBin = getTrolls(
                    previousReport,
                    report.getSubmittedAt());

            if (report.getSubmittedBy().getUserID() ==
            user.getUserID()) {
                totalTrolls += trollsAtBin;
            }

            lastReportTimes.put(binID, report.getSubmittedAt());

        }
        return totalTrolls;
    }
}


