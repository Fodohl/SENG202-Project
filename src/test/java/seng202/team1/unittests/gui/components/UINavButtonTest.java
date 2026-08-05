package seng202.team1.unittests.gui.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.components.UINavButton;
import seng202.team1.gui.screens.ScreenType;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class UINavButtonTest extends TrashAppTest {


    private UINavButton button;

    private static ScreenType NAV_TYPE = ScreenType.TIMELINE;

    @BeforeEach
    public void init(){
        button = new UINavButton(screenManager, NAV_TYPE);
    }

    // test active if active
    @Test
    public void shouldHaveActiveStyleIfMatchesScreen(){

        // Switch to this
        screenManager.showScreen(NAV_TYPE);

        assertTrue(button.getStyleClass().contains("active"));

    }

    @Test
    public void shouldNotHaveActiveStyleIfDifferentScreen(){

        // Switch away from this
        switchAway();
        assertFalse(button.getStyle().contains("active"));

    }

    @Test
    public void pressShouldSwitchToScene(){

        switchAway();

        // Simulate click
        button.getOnMouseClicked().handle(null);
        assertEquals(NAV_TYPE, screenManager.getCurrentScreen());

    }
    private void switchAway() {

        // Make sure to switch to something else
        ScreenType switchType = ScreenType.DATA;
        assert NAV_TYPE != switchType;

        screenManager.showScreen(switchType);
    }
}