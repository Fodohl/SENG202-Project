package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.layout.MainLayout;
import seng202.team1.gui.pages.Pages;

import java.util.List;

public class MainPhase extends Phase{

    public MainPhase(Router router){
        super(router, Phases.MAIN, new MainLayout(router));
    }

    @Override
    public List<Pages> allowedPages() {
        return List.of(
            Pages.HOME,
            Pages.MAP,
            Pages.TIMELINE,
            Pages.DATA,
            Pages.SETTINGS
        );
    }

    @Override
    public Pages entryPage() {
        return Pages.HOME;
    }

}
