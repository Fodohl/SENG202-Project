package seng202.team1.gui.core;

import javafx.scene.Parent;
import seng202.team1.gui.screens.ScreenType;

public abstract class LayoutElement {

    private Parent rendered = null;
    protected ScreenManager screenManager = null;
    protected ScreenType screenType = null;

    public LayoutElement(ScreenManager screenManager, ScreenType screenType){

        // Setup state
        this.screenManager = screenManager;

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

    public void onSceneChange(ScreenType destScene){

        // Dont rerender if not being shown
        if(screenType == destScene)
            return;

        // Update self
        updateContents();
    }

}
