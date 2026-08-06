package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

public class SettingsPage extends Page {

   public SettingsPage(WindowManager windowManager){

        super(windowManager, PageType.DASHBOARD);

    }

    @Override
    protected Parent renderSelf() {
        return new Label("Settings");
    }

}
