package seng202.team1.gui.core;

import javafx.scene.Parent;
import seng202.team1.gui.pages.PageType;

public abstract class LayoutElement {

    private Parent rendered = null;
    protected WindowManager windowManager = null;
    protected PageType pageType = null;

    public LayoutElement(WindowManager windowManager, PageType pageType){

        // Setup state
        this.windowManager = windowManager;

        // Register handler


    }

    protected abstract Parent render_self();

    public final Parent getRendered(){

        // Ensure element
        if(rendered == null)
            rerender();

        return rendered;

    }

    public final void rerender(){
        rendered = render_self();
    }


    public void updateContents(){
        // overridden if needed
    }

    public void onSceneChange(PageType destScene){

        // Dont rerender if not being shown
        if(pageType == destScene)
            return;

        // Update self
        updateContents();
    }

}
