package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.gui.GraphicsCommon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A UI component to handle rendering selectable buttons
 */
public class UIChipsInput extends VBox implements UIInputComponent {

    /// The label to show invalid input messages in
    private Label errorText = new Label();

    /// The container for the chip buttons
    private FlowPane chips = new FlowPane(5, 5);

    /// The currently selected chip or null if none
    private UIIconButton selectedChip = null;

    /// The butttons that form the options for the input
    private HashMap<String, UIIconButton> buttons = new HashMap<>();

    /**
     * Construct a new chips input for the values of a enum
     *
     * Note: expects a svg to exist of the lowercase enum value
     * Note: for internal values, prefix with '_' to not show
     *
     * @param options The enum that denotes the options
     * @param <T> The enum type
     */
    public <T extends Enum<T>> UIChipsInput(Class<T> options){

        // Map each enum constant to a button
        List<UIIconButton> buttons = Arrays.stream(options.getEnumConstants())
        .filter(e -> !e.name().startsWith("_"))
        .map(e -> {

            String path = String.format("/media/images/%s.svg", e.name().toLowerCase());
            String title = StringHelpers.enumNameToTitle(e.name());

            return new UIIconButton(path, title);
        }).toList();

        this(buttons.toArray(new UIIconButton[0]));
    }

    /**
     * Construct a new chips input for a given set of options
     *
     * @param options The options the user can select
     */
    public UIChipsInput(UIIconButton... options){

        super(5);

        chips.getChildren().addAll(options);

        // Map button to value
        for(UIIconButton button : options){

            String val = button.getText();
            buttons.put(val, button);

            // Setup button to be a chip
            button.getStyleClass().add("chip-button");
            button.resizeIcon(25,25);
            button.setOnMouseClicked(e -> selectChip(button.getText()));
        }

        getChildren().addAll(chips, errorText);
    }


    /**
     * Get the selected UIIconButton or null if none
     *
     * @return The selected option
     */
    @Override
    public Object getInput() {
        return selectedChip;
    }

    /**
     * Set the input to a desired value
     *
     * @param value The new string input value, must match a button
     */
    @Override
    public void setInput(Object value) {
        selectChip((String) value);

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
        return selectedChip != null;
    }

    /**
     * Reset the value of the input to default, clearing any errors
     */
    @Override
    public void clear() {
        selectChip(null);
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

    /**
     * Try select a chip for a given text input. Will unselect previous chip if any.
     *
     * @param value The chip to select
     */
    void selectChip(String value){

        // Not an option
        UIIconButton button = buttons.get(value);
        if(button == null)
            return;

        setError("");

        // Unselect previous
        if(selectedChip != null)
            GraphicsCommon.toggleClass(false, selectedChip, "active");

        // Set as selected
        selectedChip = button;
        GraphicsCommon.toggleClass(true, selectedChip, "active");
    }
}
