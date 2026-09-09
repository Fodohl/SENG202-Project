package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class UISection extends VBox {


    public UISection(Node... children) {
        this(0, children);
    }

    public UISection(Integer gap, Node... children) {
        super(gap, children);
        getStyleClass().add("section");
    }
}
