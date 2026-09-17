package seng202.team1.unittests.gui.components;

import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.status.ReportType;
import seng202.team1.gui.components.UIChipsInput;
import seng202.team1.gui.components.UIIconButton;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class UIChipsInputTest extends TrashAppTest {

     @Test
    void enumValuesShouldMapToUIButtons(){

        UIChipsInput input = new UIChipsInput(BinCategory.class);

        input.setInput("General Waste");
        assertTrue(input.hasInput());
        assertNotNull(input.getInput());
        assertEquals("General Waste", ((UIIconButton) input.getInput()).getText());

    }

     @Test
    void internalEnumValuesShouldNotBeDisplayed(){
        UIChipsInput input = new UIChipsInput(ReportType.class);

        input.setInput("Fullness Update");
        assertFalse(input.hasInput());
        assertNull(input.getInput());
    }

    @Test
    void selectChipShouldToggleActiveStyleClass() {
        UIChipsInput input = new UIChipsInput(BinCategory.class);

        input.setInput("General Waste");
        UIIconButton firstButton = (UIIconButton) input.getInput();
        assertTrue(firstButton.getStyleClass().contains("active"));

        input.setInput("Organic");
        UIIconButton secondButton = (UIIconButton) input.getInput();
        assertFalse(firstButton.getStyleClass().contains("active"));
        assertTrue(secondButton.getStyleClass().contains("active"));
    }

}