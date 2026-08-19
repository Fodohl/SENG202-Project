package seng202.team1.gui.components;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.gui.GraphicsCommon;

import java.util.List;

public class UIDropDown extends VBox implements UIInputComponent {

    private ComboBox<String> dropwDown = new ComboBox<>();
    private Label errorText = new Label();

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

    @Override
    public String getInput() {
        return dropwDown.getValue();
    }

    @Override
    public void setInput(Object value) {

        if(value instanceof  String)
            dropwDown.setValue((String)value);
    }

    @Override
    public boolean hasInput() {
        return !dropwDown.getSelectionModel().isEmpty();
    }

    @Override
    public void clear() {
        dropwDown.setValue(null);
        setError("");
    }

    @Override
    public boolean isErroring(){
        return isErroringInternal(errorText);
    }

    @Override
    public void setError(String error) {
        setIsErrorInternal(error, this, errorText);
    }
}
