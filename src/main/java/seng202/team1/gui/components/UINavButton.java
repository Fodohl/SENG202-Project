package seng202.team1.gui.components;

import seng202.team1.helpers.StringHelpers;
import seng202.team1.gui.GraphicsCommon;
import seng202.team1.gui.Router;
import seng202.team1.gui.pages.Pages;

/**
 * A UI Component to handle rendering navigation buttons
 *
 * Renders a button for a given page type, expects a svg of the same name (lowercase) to exist for it's icon. This nav
 * button will react to navigation changes and indicate if it represents the active page.
 *
 * Note: No onClick needed, will automatically handle navigation calls
 */
public class UINavButton extends UIIconButton {

    /// The router responsible for the active page and switching to others
    private Router router;

    /// The page type this nav button represents as a clickable destination
    private Pages dest;

    /**
     * Constructs a new UINavButton
     *
     * @param router The current window manger for the page this button is on
     * @param dest The navigation destination
     */
    public UINavButton(Router router, Pages dest) {

        // Setup state
        super(String.format("/media/images/%s.svg", dest.name().toLowerCase()), StringHelpers.toTitleCase(dest.name()));
        this.router = router;
        this.dest = dest;

        // Apply styles
        updateIsActive(router.getCurrentPage());

        // Handle events
        setOnMouseClicked(e -> router.switchPage(dest));
        router.registerPageChangeCallback(this::updateIsActive);
    }


    /**
     * Updates the style of this nav button to indicate whether this button's page is currently being shown or not
     *
     * @param currentActive The currently active page
     */
    private void updateIsActive(Pages currentActive){

        // No active page
        if(currentActive == null || dest == null)
            return;

        // Add styling accordingly
        GraphicsCommon.toggleClass(dest == currentActive, this, "active");
    }
}
