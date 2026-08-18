package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.route.RouteInstruction;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIImage;

import java.util.List;

public class DirectionsModal extends Modal{

    private List<RouteInstruction> instructions = null;

    public DirectionsModal(WindowManager windowManager){
        super(windowManager, Modals.DIRECTIONS);
    }

    @Override
    Parent buildBody() {

        VBox root = new VBox(10);

        for (RouteInstruction instruction : instructions)
            root.getChildren().add(buildInstructionDisplay(instruction));

        return root;
    }

    HBox buildInstructionDisplay(RouteInstruction instruction ){

        UIImage icon = new UIImage(String.format("/images/dir-%s.svg", instruction.getType().toLowerCase()), 25, 25);
        Label text = new Label(instruction.getText());
        Label meters = new Label(String.format("%sm", StringHelpers.roundToNearest(instruction.getDistance(), 50)));

        // Push the meters to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox root = new HBox(10, icon,text,spacer,meters);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    @Override
    public void recvData(Object data) {

        if(!(data instanceof List))
            return;

        instructions = (List)data;
    }
}
