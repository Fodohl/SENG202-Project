package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

public class DashboardPage extends Page {

    public DashboardPage(WindowManager windowManager){

        super(windowManager, PageType.DASHBOARD);

    }

    @Override
    protected Parent render_self() {
        return new Label("Dashboard");
    }
}
