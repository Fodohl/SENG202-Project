package seng202.team1.gui.components;

import javafx.scene.layout.HBox;

import javafx.scene.control.Label;

public class UIKeyValuePair extends HBox {

    private Label value;

    public UIKeyValuePair(String keyText, String valueText){
        super();

        // Create the pair
        Label key = new Label(keyText + ":   ");
        value = new Label(valueText);

        // Style
        key.getStyleClass().add("key");
        getStyleClass().add("kv-pair");

        // Combine
        getChildren().addAll(key, value);
    }

    public void setText(String newValue){
        value.setText(newValue);
    }
}
