package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UIParagraphInput extends VBox implements UIInputComponent {

    private TextArea textArea = new TextArea();
<<<<<<< HEAD
=======

    /// The label to show invalid input messages in
>>>>>>> main
    private Label errorText = new Label();

    public UIParagraphInput(String placeholder){
        super(5);

        // Set values
        textArea.setPromptText(placeholder);

        // Style
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setWrapText(true);
        getChildren().addAll(textArea, errorText);

        // Reset error on change
        textArea.textProperty().addListener((obs, oldVal, newVal) -> {
            setError("");
        });
    }

<<<<<<< HEAD
    @Override
    public String getInput() {
        return textArea.getText();
    }

=======
    /**
     * Get the entered text or null if none
     *
     * @return The entered input
     */
    @Override
    public String getInput() {

        return textArea.getText();
    }

    /**
     * Set the input to a desired value
     *
     * @param value The new string input value
     */
>>>>>>> main
    @Override
    public void setInput(Object value) {

        if(value instanceof  String)
            textArea.setText((String)value);
    }

<<<<<<< HEAD
    @Override
    public boolean hasInput() {
        return !textArea.getText().isEmpty();
    }

=======
    /**
     * Get whether a user has entered input yet
     *
     * Note: if true then getInput() will not be null
     *
     * @return True if the user has inputted a value
     */
    @Override
    public boolean hasInput() {

        if(getInput() == null)
            return  false;

        return !textArea.getText().isEmpty();
    }


  /**
     * Reset the value of the input to default, clearing any errors
     */
>>>>>>> main
    @Override
    public void clear() {
        textArea.setText(null);
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
