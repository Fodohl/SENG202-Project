package seng202.team1.unittests.gui.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.components.UINavButton;
import seng202.team1.gui.pages.Pages;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class UINavButtonTest extends TrashAppTest {


    private UINavButton button;

    private static Pages NAV_TYPE = Pages.TIMELINE;

    @BeforeEach
    public void init(){
        button = new UINavButton(router, NAV_TYPE);
    }

    // test active if active
    @Test
    public void shouldHaveActiveStyleIfMatchesPage(){

        // Switch to this
        router.switchPage(NAV_TYPE);

        assertTrue(button.getStyleClass().contains("active"));

    }

    @Test
    public void shouldNotHaveActiveStyleIfDifferentPage(){

        // Switch away from this
        switchAway();
        assertFalse(button.getStyle().contains("active"));

    }

    @Test
    public void pressShouldSwitchToScene(){

        switchAway();

        // Simulate click
        button.getOnMouseClicked().handle(null);
        assertEquals(NAV_TYPE, router.getCurrentPage());

    }
    private void switchAway() {

        // Make sure to switch to something else
        Pages switchType = Pages.DATA;
        assert NAV_TYPE != switchType;

        router.switchPage(switchType);
    }
}