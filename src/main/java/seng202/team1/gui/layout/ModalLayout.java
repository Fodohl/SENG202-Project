package seng202.team1.gui.layout;

import javafx.scene.Parent;
import seng202.team1.gui.WindowManager;

/**
 * Composite UI component that contains elements for a modal
 *
 */
public class ModalLayout extends LayoutElement{

    /**
     * Constuct a new ModalLayout
     * @param windowManager
     */
    public ModalLayout(WindowManager windowManager){
        super(windowManager);
    }

    /**
     * Build the layout
     *
     * @return The composited layout
     */
    @Override
    protected Parent renderSelf() {
        return null;
    }
}
