package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng202.team1.common.DateHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;

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

    private UITimeline timeline;
    private VBox timelineSection = new VBox();

    private UICard organicLogged;
    private UICard trashLogged;
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

        VBox root = new VBox(50, botCards, pageTitle, timelineSection);
        root.getStyleClass().add("timeline-page");
        renderTimelineEntries();
        return root;
    }

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

    void renderTimelineEntries() {

        List<StatusReport> reports = router.getGlobalState().getStatusReportStore().getReports();
        List<TrashLog> trash = router.getGlobalState().getTrashLogDAO().getAll();

        // Combine trash logs and status reports
        ArrayList<UITimelineEntry> entries = new ArrayList<>();
        entries.addAll(buildReportsEntries(reports));
        entries.addAll(buildTrashEntries(trash));

        timeline = new UITimeline(entries);
        timelineSection.getChildren().setAll(timeline);

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
                    Bin bin = router.getGlobalState().getBinDAO().getOne(log.binID());

                    String trashType = enumNameToTitle(log.trashType().name());


                    // Convert log into format expected by timeline entry
                    UIImage icon = new UIImage(String.format("/media/images/%s.svg", bin.getCategory().name().toLowerCase()), 50, 50);
                    String desciption = String.format("%s (%dg) logged at %s", trashType, log.trashWeight(), bin.getAreaName());

                    return new UITimelineEntry(DateHelpers.fromLocalTime(LocalDateTime.ofInstant(log.disposedAt(), ZoneId.systemDefault())), icon, "Trash Logged", desciption);
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
