package seng202.team1.unittests.gui.core;

import org.junit.jupiter.api.Test;
import seng202.team1.gui.pages.Pages;
import seng202.team1.unittests.TrashAppTest;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static seng202.team1.gui.WindowManager.START_PAGE;

class WindowManagerTest extends TrashAppTest {


    @Test
    public void entryPageShouldBeHome() {
        assertEquals(START_PAGE, windowManager.getCurrentPage());
    }

    @Test
    public void switchPageShouldUpdateCurrentPage() {

        // Switch to a page
        windowManager.switchPage(Pages.HOME);
        assertEquals(Pages.HOME, windowManager.getCurrentPage());

        // Switch to a different page
        windowManager.switchPage(Pages.MAP);
        assertEquals(Pages.MAP, windowManager.getCurrentPage());
    }

    @Test
    public void switchPageShouldTriggerCallbacks() {

        // Set up a handler
        AtomicReference<Pages> pageType = new AtomicReference<>();
        windowManager.registerPageChangeCallback(pageType::set);

        // Switch the page
        windowManager.switchPage(Pages.SETTINGS);
        assertEquals(Pages.SETTINGS, pageType.get());
    }

    @Test
    public void shouldSafleyFailToRegisterDuplicateCallback() {

        // Set up a handler
        Consumer<Pages> callback = pageType -> {};
        windowManager.registerPageChangeCallback(callback);

        // Should fail
        assertFalse(windowManager.registerPageChangeCallback(callback));
    }

    @Test
    public void backShouldGoToPreviousPage() {

        // Switch to a page
        windowManager.switchPage(Pages.HOME);
        windowManager.switchPage(Pages.MAP);

        // Go back
        windowManager.backPage();
        assertEquals(Pages.HOME, windowManager.getCurrentPage());
    }

}