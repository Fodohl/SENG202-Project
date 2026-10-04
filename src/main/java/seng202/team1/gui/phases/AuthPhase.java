package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.AuthLayout;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.pages.Pages;

import java.util.List;

/**
 * A stage in the app flow responsible for user authentication
 */
public class AuthPhase extends Phase{

    /**
     * Constructs a new AuthPhase
     *
     * @param router The router responsible for this phase
     */
    public AuthPhase(Router router){
        super(router, Phases.AUTH, new AuthLayout(router));
    }

    /**
     * The list of pages this phase expects to show
     *
     * @return The pages that can be shown
     */
    @Override
    public List<Pages> allowedPages() {
        return List.of(
            Pages.LOGIN,
            Pages.RESET,
            Pages.SIGN_UP
        );
    }

    /**
     * Get the page to show when switching to this phase
     *
     * @return The start page
     */
    @Override
    public Pages entryPage() {
        return Pages.LOGIN;
    }

}
