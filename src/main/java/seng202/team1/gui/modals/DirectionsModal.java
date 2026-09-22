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

<<<<<<< HEAD
public class DirectionsModal extends Modal{

    private List<RouteInstruction> instructions = null;

=======
/**
 * A Modal (popout) to display routing directions
 */
public class DirectionsModal extends Modal{

    /// The list of instruction steps to display
    private List<RouteInstruction> instructions = null;

    /**
     * Construct a new DirectionsModal
     *
     * @param windowManager The window manager responsible for this modal
     */
>>>>>>> main
    public DirectionsModal(WindowManager windowManager){
        super(windowManager, Modals.DIRECTIONS);
    }

<<<<<<< HEAD
=======
    /**
     * Composite subcomponents into a inner UI for this modal
     *
     * @return The UI for this modal
     */
>>>>>>> main
    @Override
    Parent buildBody() {

        VBox root = new VBox(10);

        for (RouteInstruction instruction : instructions)
            root.getChildren().add(buildInstructionDisplay(instruction));

        return root;
    }

<<<<<<< HEAD
=======
    /**
     * Build a node that formats and displays a step in the rout
     *
     * @param instruction The route instruction to render
     * @return The container of this route instruction
     */
>>>>>>> main
    HBox buildInstructionDisplay(RouteInstruction instruction ){

        UIImage icon = new UIImage(String.format("/media/images/dir-%s.svg", instruction.getType().toLowerCase()), 25, 25);
        Label text = new Label(instruction.getText());
        Label meters = new Label(String.format("%sm", StringHelpers.roundToNearest(instruction.getDistance(), 50)));

        // Push the meters to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox root = new HBox(10, icon,text,spacer,meters);
        root.setAlignment(Pos.CENTER);
        return root;
    }

<<<<<<< HEAD
=======
    /**
     * Handle data being sent to this modal
     *
     * @param data The list of routing instructions
     */
>>>>>>> main
    @Override
    public void recvData(Object data) {

        if(!(data instanceof List))
            return;

        instructions = (List)data;
    }
}
