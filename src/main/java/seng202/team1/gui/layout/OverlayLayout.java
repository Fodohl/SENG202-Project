package seng202.team1.gui.layout;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;

/**
 * Composite UI component that contains elements that are being shown over the app
 */
public class OverlayLayout extends Layout {

    /// The base container for the overlay
    StackPane root = new StackPane();

    /**
     * Constructs a new OverlayLayout
     *
     * @param router The window manager to handle closing the overlay
     */
    public OverlayLayout(Router router) {
        super(router);

        // Background overlay
        VBox background = new VBox();
        background.getStyleClass().add("overlay");
        background.setOnMouseClicked(e -> router.hideOverlay());

        root.getChildren().add(background);
    }

    /**
     * Combine UI elements to build the overlay
     *
     * @return A new root node containing the rendered overlay
     */
    @Override
    protected Parent renderSelf() {
        return root;
    }

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
            root.getChildren().removeLast();

        // Center the modal content over the overlay
        StackPane.setAlignment(contents, Pos.CENTER);
        root.getChildren().add(contents);
    }

    @Override
    public void swap(Parent rendered) {
        // overlays dont change content
    }
}
