package seng202.team1.gui.components;


import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.function.Consumer;

/**
 * A UI Component to handle dropdown input
 */
public class UISearchDropDown extends UIDropDown {

    /// The dropdown
    private ComboBox<String> dropwDown = new ComboBox<>();

    /// The label to show invalid input messages in
    private Label errorText = new Label();

    private final ObservableList<String> allOptions;
    private final FilteredList<String> filteredOptions;

    /**
     * Constructs a new dropdown
     *
     * @param prompt The default text to show when none selected
     * @param options The options to show in the dropdown
     */
    public UISearchDropDown(String prompt, List<String> options){
        super(prompt, List.of());

        // Set values
        dropwDown.setPromptText(prompt);
        dropwDown.setEditable(true);
        dropwDown.getStyleClass().add("search-dropdown");
        allOptions = FXCollections.observableArrayList(options);
        filteredOptions = new FilteredList<>(allOptions, option -> true);
        dropwDown.setItems(filteredOptions);

        dropwDown.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            event.consume();
            dropwDown.getEditor().requestFocus();
        });

        dropwDown.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            String search = newValue == null ? "" : newValue.trim().toLowerCase(Locale.ROOT);
            filteredOptions.setPredicate(option -> search.isEmpty()
                    || option.toLowerCase(Locale.ROOT).contains(search));

            if (dropwDown.isFocused() && !filteredOptions.isEmpty()) {
                dropwDown.show();
            } else if (filteredOptions.isEmpty()) {
                dropwDown.hide();
            }
        });

        // Style
        dropwDown.setMaxWidth(Double.MAX_VALUE);
        getChildren().clear();
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

    public void setOnSelectionChanged(Consumer<String> listener) {
        dropwDown.valueProperty().addListener((obs, oldValue, newValue) -> listener.accept(newValue));
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
        dropwDown.getEditor().clear();
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
