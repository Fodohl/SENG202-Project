package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng202.team1.data.export.ExportManager;
import seng202.team1.helpers.DateHelpers;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.data.export.ExportEntry;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static seng202.team1.helpers.StringHelpers.enumNameToTitle;

/**
 * Page that displays the history of a users logged trash
 */
public class TimelinePage extends Page {

    /// The timeline of the user's logged entries
    private UITimeline timeline;

    /// The container for the timeline
    private VBox timelineSection = new VBox();

    /// The stats card for how many organic items of trash logged
    private UICard organicLogged;

    /// The stats card for how many general waste items of trash logged
    private UICard trashLogged;

    /// The stats card for how many recyclable items of trash logged
    private UICard recycLogged;

    /**
     * Constructs a new TimelinePage
     *
     * @param router the window this page is on
     */
    public TimelinePage(Router router) {

        super(router, Pages.TIMELINE);

    }

    /**
     * Constructs the UI for the timeline page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        // Top cards
        trashLogged = new UICard("Total Rubbish Logged:", "150",  "", "/media/images/bin.svg");
        recycLogged = new UICard("Total Recycling Logged:", "300",  "", "/media/images/recycle.svg");
        organicLogged = new UICard("Total Organics Logged:", "450",  "", "/media/images/organic.svg");

        HBox botCards = new HBox(20, trashLogged, recycLogged, organicLogged);
        botCards.setAlignment(Pos.TOP_LEFT);

        // Cards should grow with page
        for(UICard card : List.of(organicLogged, recycLogged, trashLogged))
            HBox.setHgrow(card, Priority.ALWAYS);

         // Title
        UITitle pageTitle = new UITitle("Timeline");
        pageTitle.setAlignment(Pos.CENTER);

        UIIconButton exportButton = new UIIconButton("/media/images/export.svg", "Export Timeline");
        exportButton.getStyleClass().add("data-button");

        exportButton.setOnMouseClicked(e -> {
            List<StatusReport> reports = router.getGlobalState().getStatusReportDAO().getAll();
            List<TrashLog> trash = router.getGlobalState().getTrashLogDAO().getAll();
            StringTable table = buildExportTable(reports, trash);
            ExportManager.exportToCSV(router.getStage(), ExportManager.exportName("timeline"), table);
        });

        VBox root = new VBox(50, botCards, pageTitle, exportButton, timelineSection);
        root.getStyleClass().add("timeline-page");
        renderTimelineEntries();
        return root;
    }

    /**
     * Refreshes the page to show the latest stats and timeline entries
     */
    @Override
    public void updateContents() {


        // Get usage
        List<TrashLog> uses = router.getGlobalState().getTrashLogDAO()
                .getAll().stream()
                .sorted(Comparator.comparing(TrashLog::createdAt))
                .toList();
        List<Bin> bins = uses.stream()
                .map(b -> router.getGlobalState().getBinDAO().getOne(b.binID()))
                .toList();

        Bin newestBin = bins.isEmpty() ? null : bins.getLast();

        trashLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.GENERAL_WASTE).toList().size()));
        recycLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.RECYCLABLE).toList().size()));
        organicLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.ORGANIC).toList().size()));


        renderTimelineEntries();
    }

    /**
     * Convert all status reports and trash logs to timeline entries and add them to the timeline
     */
    void renderTimelineEntries() {

        List<StatusReport> reports = router.getGlobalState().getStatusReportDAO().getAll();
        List<TrashLog> trash = router.getGlobalState().getTrashLogDAO().getAll();

        List<UITimelineEntry> timelineEntries = new ArrayList<>();
        timelineEntries.addAll(buildReportsEntries(reports));
        timelineEntries.addAll(buildTrashEntries(trash));

        timeline = new UITimeline(timelineEntries);
        timelineSection.getChildren().setAll(timeline);
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
                .addColumn("fullness", "Fullness")
                .addColumn("issue", "Issue")
                .addColumn("trashType", "Trash Type")
                .addColumn("trashWeight", "Weight")
                .addColumn("description", "Description")
                .sortBy(Comparator.comparing(ExportEntry::time).reversed())
                .build();
    }

    /**
     * Map each status report to a timeline entry
     *
     * @param reports The list of reports the user has made
     * @return The list of converted timeline entries
     */
    private List<UITimelineEntry> buildReportsEntries(List<StatusReport> reports) {

        return reports.stream()
                .filter(r -> r.getIssue() != ReportType._FULLNESS_UPDATE)
                .map(report -> {

                    // Convert report into format expected by timeline entry
                    UIImage icon = new UIImage(String.format("/media/images/%s.svg", report.getIssue().name().toLowerCase()), 50, 50);

                    return new UITimelineEntry(report.getSubmittedAt(), icon, "Status Report", report.getDescription());
                })
                .toList();
    }


    /**
     * Map each trash log to a timeline entry
     *
     * @param trash The list of trash logs the user has made
     * @return The list of converted timeline entries
     */
    private List<UITimelineEntry> buildTrashEntries(List<TrashLog> trash) {
        return trash.stream()
                .map(log -> {

                    // Get associated log data
                    Bin bin = router.getGlobalState().getBinDAO().getOne(log.binID());

                    String trashType = enumNameToTitle(log.trashType().name());


                    // Convert log into format expected by timeline entry
                    UIImage icon = new UIImage(String.format("/media/images/%s.svg", bin.getCategory().name().toLowerCase()), 50, 50);
                    String description = String.format("%s (%.2fg) logged at %s", trashType, log.trashWeight(), bin.getAreaName());

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
