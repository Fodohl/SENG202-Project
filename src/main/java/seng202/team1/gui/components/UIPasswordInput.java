package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

public class UIPasswordInput extends VBox implements UIInputComponent {

    private final PasswordField input;
    private final Label error;

    public UIPasswordInput(String placeholder) {

        input = new PasswordField();
        input.setPromptText(placeholder);

        error = new Label();
        error.getStyleClass().add("input-error");
        error.setVisible(false);
        error.setManaged(false);

        getChildren().addAll(input, error);
    }

    @Override
    public String getInput() {
        return input.getText();
    }

    @Override
    public void setInput(Object value) {
        input.setText(value == null ? "" : value.toString());
    }

    @Override
    public boolean hasInput() {
        return !input.getText().isBlank();
    }

    @Override
    public void clear() {
        input.clear();
        clearError();
    }

    @Override
    public boolean isErroring() {
        return error.isVisible();
    }

    public void setError(String message) {
        error.setText(message);
        error.setVisible(true);
        error.setManaged(true);
    }

    public void clearError() {
        error.setText("");
        error.setVisible(false);
        error.setManaged(false);
    }
}
