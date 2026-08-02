package seng202.team1.gui.screens;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.ScreenManager;

public class LeaderboardScreen extends LayoutElement {

   public LeaderboardScreen(ScreenManager screenManager){

        super(screenManager, ScreenType.DASHBOARD);

    }

    @Override
    protected Parent render_self() {
        return new Label("Leaderboards");
    }

}
