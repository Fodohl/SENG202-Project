package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UIParagraphInput extends VBox implements UIInputComponent {

    private TextArea textArea = new TextArea();
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

    @Override
    public String getInput() {
        return textArea.getText();
    }

    @Override
    public void setInput(Object value) {

        if(value instanceof  String)
            textArea.setText((String)value);
    }

    @Override
    public boolean hasInput() {
        return !textArea.getText().isEmpty();
    }

    @Override
    public void clear() {
        textArea.setText(null);
        setError("");
    }

    @Override
    public void setError(String error) {
        setIsErrorInternal(error, this, errorText);
    }
}
