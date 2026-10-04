package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.common.DateHelpers;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.ExportManager;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.table.ExportEntry;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIIconButton;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UITimeline;
import seng202.team1.gui.components.UITimelineEntry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Page that displays the history of a users logged trash
 */
public class TimelinePage extends Page {

    private UITimeline timeline;
    private VBox root;

    /**
     * Constructs a new TimelinePage
     *
     * @param windowManager the window this page is on
     */
    public TimelinePage(WindowManager windowManager) {

        super(windowManager, Pages.TIMELINE);

    }

    /**
     * Constructs the UI for the timeline page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {


        root = new VBox(20);
        renderTimelineEntries();
        return root;
    }

    @Override
    public void updateContents() {
        renderTimelineEntries();
    }

    void renderTimelineEntries() {

        List<StatusReport> reports = windowManager.getGlobalState().getStatusReportStore().getReports();
        // TODO: Get only user generated reports
        List<TrashLog> trash = windowManager.getGlobalState().getTrashLogDAO().getAll();

        List<UITimelineEntry> timelineEntries = new ArrayList<>();
        timelineEntries.addAll(buildReportsEntries(reports));
        timelineEntries.addAll(buildTrashEntries(trash));

        UIIconButton exportButton = new UIIconButton("/media/images/export.svg", "Export Timeline Data");
        exportButton.getStyleClass().add("data-button");

        // Download the table to CSV for exports
        exportButton.setOnMouseClicked(e -> {
            StringTable table = buildExportTable(reports, trash);
            ExportManager.exportToCSV(windowManager.getStage(), ExportManager.exportName("timeline"), table);
        });
        timeline = new UITimeline(timelineEntries);
        root.getChildren().setAll(exportButton, timeline);

    }

    /**
     * Builds a combined, newest-first table of status reports and trash logs for export
     *
     * @param reports The status reports to include
     * @param trash The trash logs to include
     * @return The table, ready for ExportManager.exportToCSV
     */
    private StringTable buildExportTable(List<StatusReport> reports, List<TrashLog> trash) {
        List<ExportEntry> exportEntries = new ArrayList<>();
        reports.forEach(r -> exportEntries.add(ExportEntry.from(r)));
        trash.forEach(t -> exportEntries.add(ExportEntry.from(t)));

        return new StringTableBuilder<>(exportEntries)
                .addColumn("time", "Timestamp", o -> StringHelpers.toLocaleString((Instant) o))
                .addColumn("source", "Type")
                .addColumn("binId", "Bin ID")
                .addColumn("userId", "User ID")
                .addColumn("fullness", "Fullness")
                .addColumn("issue", "Issue")
                .addColumn("trashType", "Trash Type")
                .addColumn("trashWeight", "Weight")
                .addColumn("description", "Description")
                .sortBy(Comparator.comparing(ExportEntry::time).reversed())
                .build();
    }

    private List<UITimelineEntry> buildReportsEntries(List<StatusReport> reports) {

        return reports.stream()
                .filter(r -> r.getIssue() != ReportType._FULLNESS_UPDATE)
                .map(report -> {

                    // Convert report into format expected by timeline entry
                    UIImage icon = new UIImage(String.format("/media/images/%s.svg", report.getIssue().name().toLowerCase()), 50, 50);
                    Instant time = DateHelpers.fromLocalTime(report.getSubmittedAt());

                    return new UITimelineEntry(time, icon, "Status Report", report.getDescription());
                })
                .toList();
    }

    private List<UITimelineEntry> buildTrashEntries(List<TrashLog> trash) {
        return trash.stream()
                .map(log -> {

                    // Get associated log data
                    Bin bin = windowManager.getGlobalState().getBinDAO().getOne(log.binID());

                    String trashType = StringHelpers.enumNameToTitle(log.trashType().name());


                    // Convert log into format expected by timeline entry
                    UIImage icon = new UIImage(String.format("/media/images/%s.svg", bin.getCategory().name().toLowerCase()), 50, 50);
                    String description = String.format("%s (%dg) logged at %s", trashType, log.trashWeight(), bin.getAreaName());

                    return new UITimelineEntry(DateHelpers.fromLocalTime(LocalDateTime.ofInstant(log.disposedAt(), ZoneId.systemDefault())), icon, "Trash Logged", description);
                })
                .toList();

    }


    /**
     * Get the timeline this page displays
     *
     * @return The timeline
     */
    public UITimeline getTimeline() {
        return timeline;
    }
}
