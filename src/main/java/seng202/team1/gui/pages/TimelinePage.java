package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.common.DateHelpers;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UITimeline;
import seng202.team1.gui.components.UITimelineEntry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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


        root = new VBox(20);
        renderTimelineEntries();
        return root;
    }

    @Override
    public void updateContents() {
        renderTimelineEntries();
    }

    void renderTimelineEntries() {

        List<StatusReport> reports = getState().getStatusReportStore().getReports();
        List<TrashLog> trash = getState().getTrashLogDAO().getAll();

        // Combine trash logs and status reports
        ArrayList<UITimelineEntry> entries = new ArrayList<>();
        entries.addAll(buildReportsEntries(reports));
        entries.addAll(buildTrashEntries(trash));

        timeline = new UITimeline(entries);
        root.getChildren().setAll(timeline);

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
                    Bin bin = getState().getBinDAO().getOne(log.binID());

                    String trashType = StringHelpers.enumNameToTitle(log.trashType().name());


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
