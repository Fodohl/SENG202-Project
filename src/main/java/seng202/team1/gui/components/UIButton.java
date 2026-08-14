package seng202.team1.gui.components;


import javafx.scene.control.Button;

public class UIButton extends Button {

    public UIButton(String prompt){
        this(prompt, false);
    }

    public UIButton(String prompt, boolean secondary){
        super(prompt);

        getStyleClass().add("button");
        if(secondary)
            getStyleClass().add("secondary");
    }
}
