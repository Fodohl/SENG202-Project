package seng202.team1.unittests.gui.components;

import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.gui.components.UIChipsInput;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class UIInputComponentTest extends TrashAppTest {

    private UIChipsInput inputComponent;
    private Label errorLabel;

    @BeforeEach
    void setUp() {
        inputComponent = new UIChipsInput(BinCategory.class);
        errorLabel = new Label();
    }

    @Test
    void settingEmptyErrorShouldClearState() {
        errorLabel.setText("Must select a bin category");

        inputComponent.setIsErrorInternal("", inputComponent, errorLabel);

        assertEquals("", errorLabel.getText());
        assertFalse(inputComponent.isErroringInternal(errorLabel));
    }

    @Test
    void isErrorShouldSafleyHandleNull() {
        assertFalse(inputComponent.isErroringInternal(null));
    }

    @Test
    void setErrorShouldUpdateErrorText() {
        inputComponent.setError("Must select a bin category");

        assertTrue(inputComponent.isErroring());
        assertTrue(inputComponent.getStyleClass().contains("error"));
    }
}