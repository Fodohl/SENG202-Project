package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.WindowManager;

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

        super(windowManager, PageType.DASHBOARD);

    }

   /**
    * Constructs the UI for the timeline page
    *
    * @return the UI layout for the page
    */
   @Override
   protected Parent renderSelf() {
        return new Label("Timeline");
    }

}
