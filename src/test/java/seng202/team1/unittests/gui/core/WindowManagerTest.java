package seng202.team1.unittests.gui.core;

import org.junit.jupiter.api.Test;
import seng202.team1.gui.pages.PageType;
import seng202.team1.unittests.TrashAppTest;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class WindowManagerTest extends TrashAppTest {


    @Test
    public void entryPageShouldBeDashboard() {
        assertEquals(PageType.DASHBOARD, windowManager.getCurrentPage());
    }

    @Test
    public void switchPageShouldUpdateCurrentPage() {

        // Switch to a page
        windowManager.switchPage(PageType.DASHBOARD);
        assertEquals(PageType.DASHBOARD, windowManager.getCurrentPage());

        // Switch to a different page
        windowManager.switchPage(PageType.MAP);
        assertEquals(PageType.MAP, windowManager.getCurrentPage());
    }

    @Test
    public void switchPageShouldTriggerCallbacks() {

        // Set up a handler
        AtomicReference<PageType> pageType = new AtomicReference<>();
        windowManager.registerPageChangeCallback(pageType::set);

        // Switch the page
        windowManager.switchPage(PageType.SETTINGS);
        assertEquals(PageType.SETTINGS, pageType.get());
    }

    @Test
    public void shouldSafleyFailToRegisterDuplicateCallback() {

        // Set up a handler
        Consumer<PageType> callback = pageType -> {};
        windowManager.registerPageChangeCallback(callback);

        // Should fail
        assertFalse(windowManager.registerPageChangeCallback(callback));
    }

    @Test
    public void backShouldGoToPreviousPage() {

        // Switch to a page
        windowManager.switchPage(PageType.DASHBOARD);
        windowManager.switchPage(PageType.MAP);

        // Go back
        windowManager.backPage();
        assertEquals(PageType.DASHBOARD, windowManager.getCurrentPage());
    }

}