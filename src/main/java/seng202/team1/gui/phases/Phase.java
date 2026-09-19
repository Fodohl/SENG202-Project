package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.layout.LayoutElement;
import seng202.team1.gui.pages.Pages;

import java.util.List;

public abstract class Phase {

    /// The type of phase this is
    private Phases phase;

    /// The reference to the router that handles this window
    private Router router;

    private Layout layout;

    public Phase(Router router, Phases phase, Layout layout){
        this.phase = phase;
        this.router = router;
        this.layout = layout;
    }

    public abstract List<Pages> allowedPages();

    public Layout getLayout() {
        return layout;
    }

    public abstract Pages entryPage();

    public boolean allowsPage(Pages page){
        return allowedPages().contains(page);
    }
}
