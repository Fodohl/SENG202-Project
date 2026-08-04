package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class UITitle extends Label {

    public UITitle(String title){

        setText(title);
        getStyleClass().add("title");
        setAlignment(Pos.CENTER);
    }

}
