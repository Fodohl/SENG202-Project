package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.layout.MainLayout;
import seng202.team1.gui.pages.Pages;

import java.util.List;

/**
 * A stage in the app flow responsible for primary use
 */
public class MainPhase extends Phase{

    /**
     * Constructs a new MainPhase
     *
     * @param router The router responsible for this phase
     */
    public MainPhase(Router router){
        super(router, Phases.MAIN, new MainLayout(router));
    }

    /**
     * The list of pages this phase expects to show
     *
     * @return The pages that can be shown
     */
    @Override
    public List<Pages> allowedPages() {
        return List.of(
            Pages.HOME,
            Pages.MAP,
            Pages.COMMUNITY,
            Pages.TIMELINE,
            Pages.DATA,
            Pages.SETTINGS
        );
    }

    /**
     * Get the page to show when switching to this phase
     *
     * @return The start page
     */
    @Override
    public Pages entryPage() {
        return Pages.HOME;
    }

}
