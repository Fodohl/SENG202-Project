package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.layout.PreflightLayout;
import seng202.team1.gui.pages.Pages;

import java.util.List;

/**
 * A stage in the app flow responsible for app setup
 */
public class PreflightPhase extends Phase {

     /**
     * Constructs a new PreflightPhase
     *
     * @param router The router responsible for this phase
     */
    public PreflightPhase(Router router){
        super(router, Phases.PREFLIGHT, new PreflightLayout(router));
    }

    /**
     * The list of pages this phase expects to show
     *
     * @return The pages that can be shown
     */
    @Override
    public List<Pages> allowedPages() {
        return List.of(
            Pages.STARTING
        );
    }

    /**
     * Get the page to show when switching to this phase
     *
     * @return The start page
     */
    @Override
    public Pages entryPage() {
        return Pages.STARTING;
    }
}
