package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.WindowManager;

/**
 * Page that displays a overview of the TrashApp
 */
public class DashboardPage extends Page {

    /**
     * Constructs a new DashboardPage
     *
     * @param windowManager the window this page is on
     */
    public DashboardPage(WindowManager windowManager){

        super(windowManager, PageType.DASHBOARD);

    }

    /**
     * Constructs the UI for the dashboard page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {
        return new Label("Dashboard");
    }
}
