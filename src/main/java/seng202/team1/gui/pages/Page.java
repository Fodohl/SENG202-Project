package seng202.team1.gui.pages;

import seng202.team1.gui.layout.LayoutElement;
import seng202.team1.gui.Router;

/**
 * A {@link LayoutElement} that represents a page
 */
public abstract class Page extends LayoutElement {

    /// The type of page this UI component represents
    protected Pages pageType = null;

    /**
     * Constructs a new Page
     *
     * @see #Page(Router, Pages, boolean)
     */
    public Page(Router router, Pages pageType) {
        this(router, pageType, false);
    }

    /**
     * Constructs a new Page
     *
     * @param router The window this page is on
     * @param pageType The type of page this is
     * @param preRender Whether to render during construction or not
     */
    public Page(Router router, Pages pageType, boolean preRender){

        super(router, preRender);

        // Register handler for page changes
        this.pageType = pageType;
        this.router.registerPageChangeCallback(this::onPageChange);

    }

    /**
     * Update the contents of the page without performing a full re-render
     */
    public void updateContents(){
        // overridden only if needed
    }

    /**
     * Updates the page contents when this page is about to be shown
     *
     * @param destScene The page that has been switched to
     */
    private void onPageChange(Pages destScene){

        // Dont rerender if not being shown
        if(pageType != destScene)
            return;

        // Update self
        updateContents();
    }
}
