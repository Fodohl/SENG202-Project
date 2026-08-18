package seng202.team1.gui.modals;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.gui.WindowManager;

public class DirectionsModal extends Modal{

    public DirectionsModal(WindowManager windowManager){
        super(windowManager, Modals.DIRECTIONS);
    }

    @Override
    Parent buildBody() {
        return new VBox();
    }
}
