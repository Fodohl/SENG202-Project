package seng202.team1.gui.layout;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import seng202.team1.gui.Router;

public abstract class Layout extends LayoutElement{

    public Layout(Router router){
        super(router);
    }


    public abstract void swap(Parent rendered);
}
