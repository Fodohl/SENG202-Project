package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.GraphicsCommon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UIChipsInput extends VBox implements UIInputComponent {


    private Label errorText = new Label();
    private FlowPane chips = new FlowPane(5, 5);

    private UIIconButton selectedChip = null;

    private HashMap<String, UIIconButton> buttons = new HashMap<>();


    public <T extends Enum<T>> UIChipsInput(Class<T> options){

        List<UIIconButton> buttons = Arrays.stream(options.getEnumConstants()).map(e -> {

            String path = String.format("/media/images/%s.svg", e.name().toLowerCase());
            String title = StringHelpers.enumNameToTitle(e.name());

            return new UIIconButton(path, title);
        }).toList();

        this(buttons.toArray(new UIIconButton[0]));
    }

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


    @Override
    public Object getInput() {
        return selectedChip;
    }

    @Override
    public void setInput(Object value) {
        selectChip((String) value);

    }

    @Override
    public boolean hasInput() {
        return selectedChip != null;
    }

    @Override
    public void clear() {
        selectChip(null);
    }

    @Override
    public boolean isErroring(){
        return isErroringInternal(errorText);
    }

    @Override
    public void setError(String isError) {
        setIsErrorInternal(isError, this, errorText);
    }

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
