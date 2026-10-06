package seng202.team1.gui.components;


import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * A dropdown the user can type into and filters the options
 */
public class UISearchDropDown extends UIDropDown {

    /// dropdown
    private TextField searchInput = new TextField();

    /// The label to show invalid input messages in
    private Label errorText = new Label();

    /// The options the user can pick from
    private final List<String> options;

    /// The option that matches, or null
    private final ReadOnlyObjectWrapper<String> selected = new ReadOnlyObjectWrapper<>();

    /// The popup with options as the user types
    private final AutoCompletionBinding<String> autoCompletion;

    /**
     * Makes a new search dropdown
     *
     * @param prompt The default text to show when none selected
     * @param options The options to show
     */
    public UISearchDropDown(String prompt, List<String> options){
        super(prompt, List.of());

        this.options = List.copyOf(options);

        // Set values
        searchInput.setPromptText(prompt);
        searchInput.getStyleClass().add("search-dropdown");
        autoCompletion = TextFields.bindAutoCompletion(searchInput, request -> matchingOptions(request.getUserText()));

        // Show options when clicked
        searchInput.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> autoCompletion.setUserInput(searchInput.getText()));
        searchInput.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if(isFocused)
                autoCompletion.setUserInput(searchInput.getText());
        });

        // Only count the text as a selection once it matches an option
        searchInput.textProperty().addListener((obs, oldValue, newValue) -> selected.set(findOption(newValue)));

        // Style
        searchInput.setMaxWidth(Double.MAX_VALUE);
        getChildren().clear();
        getChildren().addAll(searchInput, errorText);

        // Reset error on change
        selected.addListener((obs, oldVal, newVal) -> setError(""));
    }

    /**
     * Get the options containing some text, ignoring case
     * (LLM helped to make this method)
     * 
     * @param text The text to search for
     * @return The matching options, or all options if the text is empty
     */
    private List<String> matchingOptions(String text){

        String search = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
        return options.stream().filter(option -> option.toLowerCase(Locale.ROOT).contains(search)).toList();
    }

    /**
     * Find the option that matches
     *
     * @param text The text to match
     * @return The matching option, or null
     */
    private String findOption(String text){

        if(text == null)
            return null;

        String search = text.trim();
        return options.stream().filter(option -> option.equalsIgnoreCase(search)).findFirst().orElse(null);
    }

     /**
     * Get the selected option or null if none
     *
     * @return The selected option
     */
    @Override
    public String getInput() {
        return selected.get();
    }

    /**
     * Set the input to a desired value
     *
     * @param value The new string input value
     */
    @Override
    public void setInput(Object value) {

        if(value instanceof String)
            searchInput.setText((String)value);
    }

    /**
     * Run a listener whenever it changes
     *
     * @param listener Called with the new value
     */
    public void setOnSelectionChanged(Consumer<String> listener) {
        selected.addListener((obs, oldValue, newValue) -> listener.accept(newValue));
    }

    /**
     * Get whether a user has entered input yet
     *
     * Note: if true then getInput() will not be null
     *
     * @return True if the user has selected something
     */
    @Override
    public boolean hasInput() {
        return selected.get() != null;
    }

    /**
     * Reset the value of the input to default, clearing any errors
     */
    @Override
    public void clear() {
        searchInput.clear();
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
