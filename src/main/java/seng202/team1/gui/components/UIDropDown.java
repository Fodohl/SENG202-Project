package seng202.team1.gui.components;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.gui.GraphicsCommon;

import java.util.List;

<<<<<<< HEAD
public class UIDropDown extends VBox implements UIInputComponent {

    private ComboBox<String> dropwDown = new ComboBox<>();
    private Label errorText = new Label();

=======
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
>>>>>>> main
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

<<<<<<< HEAD
=======
     /**
     * Get the selected dropdown value or null if none
     *
     * @return The selected option
     */
>>>>>>> main
    @Override
    public String getInput() {
        return dropwDown.getValue();
    }

<<<<<<< HEAD
=======
    /**
     * Set the input to a desired value
     *
     * @param value The new string input value, must match a dropdown
     */
>>>>>>> main
    @Override
    public void setInput(Object value) {

        if(value instanceof  String)
            dropwDown.setValue((String)value);
    }

<<<<<<< HEAD
=======
    /**
     * Get whether a user has entered input yet
     *
     * Note: if true then getInput() will not be null
     *
     * @return True if the user has inputted a value
     */
>>>>>>> main
    @Override
    public boolean hasInput() {
        return !dropwDown.getSelectionModel().isEmpty();
    }

<<<<<<< HEAD
=======
    /**
     * Reset the value of the input to default, clearing any errors
     */
>>>>>>> main
    @Override
    public void clear() {
        dropwDown.setValue(null);
        setError("");
    }

<<<<<<< HEAD
=======
    /**
     * Check if the current input has resulted in an error
     *
     * @return True if there is an error set
     */
>>>>>>> main
    @Override
    public boolean isErroring(){
        return isErroringInternal(errorText);
    }

<<<<<<< HEAD
=======
    /**
     * Set the error for this input
     *
     * @param error The error message
     */
>>>>>>> main
    @Override
    public void setError(String error) {
        setIsErrorInternal(error, this, errorText);
    }
}
