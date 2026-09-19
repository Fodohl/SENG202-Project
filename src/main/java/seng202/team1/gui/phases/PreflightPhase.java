package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.layout.PreflightLayout;
import seng202.team1.gui.pages.Pages;

import java.util.List;

public class PreflightPhase extends Phase {

    public PreflightPhase(Router router){
        super(router, Phases.PREFLIGHT, new PreflightLayout(router));
    }

    @Override
    public List<Pages> allowedPages() {
        return List.of(
            Pages.STARTING
        );
    }

    @Override
    public Pages entryPage() {
        return Pages.STARTING;
    }
}
