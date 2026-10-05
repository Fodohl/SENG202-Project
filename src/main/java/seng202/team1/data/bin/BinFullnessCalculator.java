package seng202.team1.data.bin;

import seng202.team1.database.DAO.BinDAO;
import seng202.team1.database.DAO.StatusReportDAO;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.status.StatusReport;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class BinFullnessCalculator {

    public static void refreshBinFullness(Bin bin, BinDAO binDAO, StatusReportDAO statusReportDAO) {
        Instant startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        // Only want recent reports of fullness
        List<StatusReport> reportsFoBin = statusReportDAO.getFullnessReportsByBinId(bin.getID()).stream()
                .filter(r -> r.getSubmittedAt().isAfter(startOfDay))
                .toList();

        refreshBinFullness(bin, binDAO, reportsFoBin);
    }

    public static void refreshBinFullness(Bin bin, BinDAO binDAO, List<StatusReport> reportsFoBin) {
        bin.updateFullness(binDAO, reportsFoBin);
    }
}
