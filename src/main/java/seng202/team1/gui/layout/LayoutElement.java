package seng202.team1.gui.layout;

import javafx.scene.Parent;
import seng202.team1.gui.WindowManager;
import seng202.team1.state.GlobalState;

/**
 * A composite UI component
 *
 * Handles management of when to render and update
 */
public abstract class LayoutElement implements Cloneable{

    /// The rendered component, null until the first render call unless pre-rendered
    private Parent rendered;

    /// The reference to the window this component is rendered on
    protected WindowManager windowManager;

    /**
     * Construct a new lazy-rendered LayoutElement
     * 
     * @see #LayoutElement(WindowManager, boolean)
     */
    public LayoutElement(WindowManager windowManager){
        this(windowManager, false);
    }
    /**
     * Construct a new LayoutElement
     *
     * @param windowManager A reference to the window this component will be rendered to
     * @param preRender Whether to render during construction or not
     */
    public LayoutElement(WindowManager windowManager, boolean preRender){

        // Setup state
        this.windowManager = windowManager;

        // Some larger components should pre-render to not lag when user presses nav button
        if(preRender && !GlobalState.IS_HEADLESS_MODE)
            rerender();

    }

    /**
     * Construct the UI for the component
     *
     * @return A new node containing the component's UI elements
     */
    protected abstract Parent renderSelf();

    /**
     * Get the rendered content of this component.
     *
     * Note: lazy renders, if this is for a large component should pass preRender = true to constructor
     *
     * @return The content of this component
     */
    public final Parent getRendered(){

        // Ensure element
        if(rendered == null)
            rerender();

        return rendered;
    }

    /**
     * Renders the component and stores result for next render call
     */
    public final void rerender(){
        rendered = renderSelf();
    }

    @Override
    public LayoutElement clone() {
        try {
            LayoutElement clone = (LayoutElement) super.clone();

            // Force rerender
            clone.rendered = null;
            return clone;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

<<<<<<< HEAD
=======
    /**
     * Get the window manager responsible for this component
     *
     * @return The window manager responsible for this component
     */
>>>>>>> main
    public WindowManager getWindow(){
        return windowManager;
    }
}
