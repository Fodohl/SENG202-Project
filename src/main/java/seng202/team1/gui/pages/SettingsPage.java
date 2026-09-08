package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

/**
 * Page that displays the configuration menu of the app
 */
public class SettingsPage extends Page {

    /**
     * Constructs a new SettingsPage
     *
     * @param windowManager the window this page is on
     */
   public SettingsPage(WindowManager windowManager){

        super(windowManager, PageType.DASHBOARD);

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
