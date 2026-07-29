package seng202.team1.gui.core;

import javafx.scene.Parent;

public abstract class LayoutElement {

    private Parent rendered = null;

    protected abstract Parent render_self();

    public final Parent getRendered(){

        // Ensure element
        if(rendered == null)
            rerender();

        return rendered;

    }

    public void updateContents(){
        // overridden if needed
    }

    public final void rerender(){
        rendered = render_self();
    }
}
