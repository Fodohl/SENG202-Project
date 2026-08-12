package seng202.team1.gui.components;


import javafx.scene.control.Button;

public class UIButton extends Button {

    public UIButton(String prompt){
        super(prompt);

        getStyleClass().add("button");
    }
}
