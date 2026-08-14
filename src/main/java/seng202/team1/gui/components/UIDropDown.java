package seng202.team1.gui.components;


import javafx.scene.control.ComboBox;

import java.util.List;

public class UIDropDown extends ComboBox<String> {

    public UIDropDown(String prompt, List<String> options){

        // Set values
        setPromptText(prompt);
        getItems().addAll(options);

        // Style
        setMaxWidth(Double.MAX_VALUE);

    }
}
