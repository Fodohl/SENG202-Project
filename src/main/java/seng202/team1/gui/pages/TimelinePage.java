package seng202.team1.gui.pages;

import javafx.scene.Parent;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UITimeline;
import seng202.team1.gui.components.UITimelineEntry;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Page that displays the history of a users logged trash
 */
public class TimelinePage extends Page {

   /**
    * Constructs a new TimelinePage
    *
    * @param windowManager the window this page is on
    */
   public TimelinePage(WindowManager windowManager){

        super(windowManager, Pages.TIMELINE);

    }

   /**
    * Constructs the UI for the timeline page
    *
    * @return the UI layout for the page
    */
   @Override
   protected Parent renderSelf() {
        Instant now = Instant.now();
        Instant twoDaysAgotwoDaysAgo = now.minus(2, ChronoUnit.DAYS);
        Instant fiveDaysAgo = now.minus(5, ChronoUnit.DAYS);

        UIImage icon = new UIImage("/media/images/bin.svg", 50, 50);
        UIImage icon2 = new UIImage("/media/images/full.svg", 50, 50);
        UIImage icon3 = new UIImage("/media/images/recycle.svg", 50, 50);

        UITimelineEntry e1 = new UITimelineEntry(now, icon, "T1", "ST1");
        UITimelineEntry e2 = new UITimelineEntry(twoDaysAgotwoDaysAgo, icon2, "T1", "ST1");
        UITimelineEntry e3 = new UITimelineEntry(fiveDaysAgo, icon3, "T1", "ST1");

        List<UITimelineEntry> entries = Arrays.asList(e1, e2, e3);
        UITimeline timeline = new UITimeline(entries);

        return timeline;
    }

}
