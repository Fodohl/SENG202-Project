package seng202.team1.gui.components;

import javafx.scene.layout.HBox;

import javafx.scene.control.Label;

/**
 * A UI Component to handle a value and its name
 */
public class UIKeyValuePair extends HBox {

    /// The value field
    private Label value;

    /**
     * Construct a new UIKeyValuePair
     *
     * @param keyText The text to display for the key
     * @param valueText The value to display
     */
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

    /**
     * Update the value to be a new string
     *
     * @param newValue The new value for this key
     */
    public void setText(String newValue){
        value.setText(newValue);
    }
}
