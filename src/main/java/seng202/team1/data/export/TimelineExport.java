package seng202.team1.data.export;

import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.database.DAO.StatusReportDAO;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.state.GlobalState;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimelineExport {

    public static void exportTimeline(GlobalState state) {

        // Get local data table
        StatusReportDAO reportDAO = state.getLocalTables().getStatusReportsTable();
        TrashLogDAO trashLogDAO = state.getLocalTables().getTrashTable();

        // Get the data for the export
        List<StatusReport> reports = reportDAO.getNonFullnessReports();
        List<TrashLog> trash = trashLogDAO.getAll();

        // Convert to CSV and save
        StringTable table = buildExportTable(reports, trash, reportDAO);
        ExportManager.exportToCSV(state.getRouter().getStage(), ExportManager.exportName("timeline"), table);
    }

    /**
     * Builds a combined, newest-first table of status reports and trash logs for export
     *
     * @param reports The status reports to include
     * @param trash   The trash logs to include
     * @return The table, ready for ExportManager.exportToCSV
     */
    private  static StringTable buildExportTable(List<StatusReport> reports, List<TrashLog> trash, StatusReportDAO reportDAO) {
        List<ExportEntry> exportEntries = new ArrayList<>();
        reports.forEach(r -> exportEntries.add(ExportEntry.from(r)));

        // TrashLog and BinFullness from associated StatusReport
        trash.forEach(t -> exportEntries.add(ExportEntry.from(t,
                reportDAO.getFullnessByTrashLog(t))));

        return new StringTableBuilder<>(exportEntries)
                .addColumn("time", "Timestamp", entry -> StringHelpers.toLocaleString(entry.time()))
                .addColumn("source", "Type")
                .addColumn("binId", "Bin ID")
                .addColumn("fullness", "Fullness")
                .addColumn("issue", "Issue")
                .addColumn("trashType", "Trash Type")
                .addColumn("trashWeight", "Weight")
                .addColumn("description", "Description")
                .sortBy(Comparator.comparing(ExportEntry::time).reversed())
                .build();
    }


}
