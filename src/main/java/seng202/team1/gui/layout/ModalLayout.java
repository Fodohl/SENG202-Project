package seng202.team1.gui.layout;

import javafx.scene.Parent;
import seng202.team1.gui.Router;

/**
 * Composite UI component that contains elements for a modal
 *
 */
public class ModalLayout extends Layout{

    /**
     * Constuct a new ModalLayout
     * @param router
     */
    public ModalLayout(Router router){
        super(router);
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

    @Override
    public void swap(Parent rendered) {
        // modals content isnt dynamic
    }
}
