package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.layout.LayoutElement;
import seng202.team1.gui.pages.Pages;

import java.util.List;

/**
 * A stage in the app flow that shows a subset of pages on a given layout
 */
public abstract class Phase {

    /// The type of phase this is
    private Phases phase;

    /// The reference to the router that handles this window
    private Router router;

    /// The layout that the pages are placed into
    private Layout layout;

    /**
     * Constructs a new phase
     *
     * @param router The router responsible for this phase
     * @param phase The type of phase this is
     * @param layout The layout for this phase
     */
    public Phase(Router router, Phases phase, Layout layout){
        this.phase = phase;
        this.router = router;
        this.layout = layout;
    }

    /**
     * The list of pages this phase expects to show
     *
     * @return The pages that can be shown
     */
    public abstract List<Pages> allowedPages();

    /**
     * Get the layout to use for this phase
     *
     * @return The layout
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * Get the page to show when switching to this phase
     *
     * @return The start page
     */
    public abstract Pages entryPage();

    /**
     * Check if a give page is valid for this phase
     *
     * @param page The page to check
     * @return True if it is valid
     */
    public boolean allowsPage(Pages page){
        return allowedPages().contains(page);
    }
}
