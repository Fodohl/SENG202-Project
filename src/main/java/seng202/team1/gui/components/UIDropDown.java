package seng202.team1.gui.components;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.gui.GraphicsCommon;

import java.util.List;

/**
 * A UI Component to handle dropdown input
 */
public class UIDropDown extends VBox implements UIInputComponent {

    /// The dropdown
    private ComboBox<String> dropwDown = new ComboBox<>();

    /// The label to show invalid input messages in
    private Label errorText = new Label();

    /**
     * Constructs a new dropdown
     *
     * @param prompt The default text to show when none selected
     * @param options The options to show in the dropdown
     */
    public UIDropDown(String prompt, List<String> options){
        super(5);

        // Set values
        dropwDown.setPromptText(prompt);
        dropwDown.getItems().addAll(options);

        // Style
        dropwDown.setMaxWidth(Double.MAX_VALUE);
        getChildren().addAll(dropwDown, errorText);

        // Reset error on change
        dropwDown.valueProperty().addListener((obs, oldVal, newVal) -> {
            setError("");
        });
    }

     /**
     * Get the selected dropdown value or null if none
     *
     * @return The selected option
     */
    @Override
    public String getInput() {
        return dropwDown.getValue();
    }

    /**
     * Set the input to a desired value
     *
     * @param value The new string input value, must match a dropdown
     */
    @Override
    public void setInput(Object value) {

        if(value instanceof  String)
            dropwDown.setValue((String)value);
    }

    /**
     * Get whether a user has entered input yet
     *
     * Note: if true then getInput() will not be null
     *
     * @return True if the user has inputted a value
     */
    @Override
    public boolean hasInput() {
        return !dropwDown.getSelectionModel().isEmpty();
    }

    /**
     * Reset the value of the input to default, clearing any errors
     */
    @Override
    public void clear() {
        dropwDown.setValue(null);
        setError("");
    }

    /**
     * Check if the current input has resulted in an error
     *
     * @return True if there is an error set
     */
    @Override
    public boolean isErroring(){
        return isErroringInternal(errorText);
    }

    /**
     * Set the error for this input
     *
     * @param error The error message
     */
    @Override
    public void setError(String error) {
        setIsErrorInternal(error, this, errorText);
    }
}
