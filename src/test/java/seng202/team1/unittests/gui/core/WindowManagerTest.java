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
    public void entryPageShouldBeDashboard() {
        assertEquals(START_PAGE, windowManager.getCurrentPage());
    }

    @Test
    public void switchPageShouldUpdateCurrentPage() {

        // Switch to a page
        windowManager.switchPage(Pages.DASHBOARD);
        assertEquals(Pages.DASHBOARD, windowManager.getCurrentPage());

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
        windowManager.switchPage(Pages.DASHBOARD);
        windowManager.switchPage(Pages.MAP);

        // Go back
        windowManager.backPage();
        assertEquals(Pages.DASHBOARD, windowManager.getCurrentPage());
    }

}