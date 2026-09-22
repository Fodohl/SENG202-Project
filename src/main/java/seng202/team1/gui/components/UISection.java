package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

<<<<<<< HEAD
public class UISection extends VBox {


=======
/**
 * A UI Component to handle breaking nodes into sections
 */
public class UISection extends VBox {

    /**
     * Constructs a new UISection
     *
     * @see #UISection(Integer, Node...)
     */
>>>>>>> main
    public UISection(Node... children) {
        this(0, children);
    }

<<<<<<< HEAD
=======
    /**
     * Constructs a new UISection
     *
     * @param gap The gap between nodes (in pixels)
     * @param children The components that make up this section
     */
>>>>>>> main
    public UISection(Integer gap, Node... children) {
        super(gap, children);
        getStyleClass().add("section");
    }
}
