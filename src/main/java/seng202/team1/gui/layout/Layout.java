package seng202.team1.gui.layout;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import seng202.team1.gui.Router;

/**
 * Composite UI component that contains elements consistent across a stage of the app
 *
 * Note: there should be only one per phase
 */
public abstract class Layout extends LayoutElement{

    public Layout(Router router){
        super(router);
    }


    public abstract void swap(Parent rendered);
}
