package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.WindowManager;

/**
 * Page that displays a ranking of the TrashApps users
 */
public class LeaderboardPage extends Page {

   /**
    * Constructs a new LeaderboardPage
    *
    * @param windowManager the window this page is on
    */
   public LeaderboardPage(WindowManager windowManager){

        super(windowManager, Pages.DASHBOARD);

   }

   /**
    * Constructs the UI for the leaderboard page
    *
    * @return the UI layout for the page
    */
   @Override
   protected Parent renderSelf() {
        return new Label("Leaderboards");
    }

}
