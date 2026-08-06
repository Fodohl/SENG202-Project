package seng202.team1.gui.pages;

import seng202.team1.gui.core.LayoutElement;
import seng202.team1.gui.core.WindowManager;

public abstract class Page extends LayoutElement {


    /// The type of page this UI component represents
    protected PageType pageType = null;


    public Page(WindowManager windowManager, PageType pageType) {
        this(windowManager, pageType, false);
    }

    public Page(WindowManager windowManager, PageType pageType, boolean preRender){

        super(windowManager, preRender);

        // Register handler for page changes
        this.pageType = pageType;
        this.windowManager.registerPageChangeCallback(this::onPageChange);

    }


    public void updateContents(){
        // overridden only if needed
    }

    public void onPageChange(PageType destScene){

        // Dont rerender if not being shown
        if(pageType == destScene)
            return;

        // Update self
        updateContents();
    }

}
