package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * A UI Component to handle rendering titles
 *
 * Provides consistent styling and centering for a TrashApp titles
 */
public class UITitle extends Label {

    /**
     * Constructs a new title
     *
     * @param title The title to show
     */
    public UITitle(String title){

        setText(title);
        getStyleClass().add("title");
        setAlignment(Pos.CENTER);
    }

}
