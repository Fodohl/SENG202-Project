package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UITextInput extends VBox implements UIInputComponent{

    private TextField textField = new TextField();
    private Label errorText = new Label();

    public UITextInput(String placeholder){
        super(5);

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

    @Override
    public String getInput() {
        return textField.getText();
    }

    @Override
    public void setInput(Object value) {

        if(value instanceof  String)
            textField.setText((String)value);
    }

    @Override
    public boolean hasInput() {
        return !textField.getText().isEmpty();
    }

    @Override
    public void clear() {
        textField.setText(null);
        setError("");
    }

    @Override
    public void setError(String error) {
        setIsErrorInternal(error, this, errorText);
    }
}
