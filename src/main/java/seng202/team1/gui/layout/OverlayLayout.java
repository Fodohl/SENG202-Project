package seng202.team1.gui.layout;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.WindowManager;

<<<<<<< HEAD
public class OverlayLayout extends LayoutElement {

    StackPane root = new StackPane();

=======
/**
 * Composite UI component that contains elements that are being shown over the app
 * <p>
 * Note: there should be only one per window
 */
public class OverlayLayout extends LayoutElement {

    /// The base container for the overlay
    StackPane root = new StackPane();

    /**
     * Constructs a new OverlayLayout
     *
     * @param windowManager The window manager to handle closing the overlay
     */
>>>>>>> main
    public OverlayLayout(WindowManager windowManager) {
        super(windowManager);

        // Background overlay
        VBox background = new VBox();
        background.getStyleClass().add("overlay");
        background.setOnMouseClicked(e -> windowManager.hideOverlay());

        root.getChildren().add(background);
    }

<<<<<<< HEAD
=======
    /**
     * Combine UI elements to build the overlay
     *
     * @return A new root node containing the rendered overlay
     */
>>>>>>> main
    @Override
    protected Parent renderSelf() {
        return root;
    }

<<<<<<< HEAD

    public void setVisible(boolean visible){
        root.setVisible(visible);
    }

    public void setContents(Parent contents){

        // Remove old contents
        while(root.getChildren().size() > 1)
=======
    /**
     * Set the whetehr the modal can be seen or not
     *
     * @param visible True to show the modal, false to hide it
     */
    public void setVisible(boolean visible) {
        root.setVisible(visible);
    }

    /**
     * Set the overlay layout's inner node
     *
     * @param contents The node to overlay on the app
     */
    public void setContents(Parent contents) {

        // Remove old contents
        while (root.getChildren().size() > 1)
>>>>>>> main
            root.getChildren().removeLast();

        // Center the modal content over the overlay
        StackPane.setAlignment(contents, Pos.CENTER);
        root.getChildren().add(contents);
    }
}
