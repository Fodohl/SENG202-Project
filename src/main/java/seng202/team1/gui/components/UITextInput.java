package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;

public class UITextInput extends VBox implements UIInputComponent{

    private TextInputControl textField = new TextField();

    /// The label to show invalid input messages in
    private Label errorText = new Label();

    private boolean isPrivate = false;

    private String prompt;

    public UITextInput(String placeholder){
        super(5);
        prompt = placeholder;

        // Set values
        textField.setPromptText(placeholder);

        // Style
        textField.setMaxWidth(Double.MAX_VALUE);
        getChildren().addAll(textField, errorText);

        // Reset error on change
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            setError("");
        });
    }

    /**
     * Get the entered text or null if none
     *
     * @return The entered input
     */
    @Override
    public String getInput() {
        return textField.getText();
    }

        /**
     * Set the input to a desired value
     *
     * @param value The new string input value
     */
    @Override
    public void setInput(Object value) {

        if(value instanceof String)
            textField.setText((String)value);
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

        if(textField.getText() == null)
            return false;

        return !textField.getText().isEmpty();
    }

     /**
     * Reset the value of the input to default, clearing any errors
     */
    @Override
    public void clear() {
        textField.setText(null);
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


    public void setPrivate(){

        // Already privated
        if(isPrivate)
            return;

        // Replace old input with password
        getChildren().remove(textField);
        textField = new PasswordField();
        textField.setPromptText(prompt);

        // Add to the ui
        getChildren().add(0, textField);

        isPrivate = true;
    }
}
