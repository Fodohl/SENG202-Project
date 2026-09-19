package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.Router;

/**
 * Page that displays the configuration menu of the app
 */
public class SettingsPage extends Page {

    /**
     * Constructs a new SettingsPage
     *
     * @param router the window this page is on
     */
   public SettingsPage(Router router){

        super(router, Pages.SETTINGS);

    }

   /**
    * Constructs the UI for the settings page
    *
    * @return the UI layout for the page
    */
   @Override
   protected Parent renderSelf() {
        return new Label("Settings");
    }

}
