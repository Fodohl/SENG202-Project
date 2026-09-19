package seng202.team1.unittests.gui.core;

import org.junit.jupiter.api.Test;
import seng202.team1.gui.pages.Pages;
import seng202.team1.unittests.TrashAppTest;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static seng202.team1.gui.Router.START_PAGE;

class RouterTest extends TrashAppTest {


    @Test
    public void entryPageShouldBeDashboard() {
        assertEquals(START_PAGE, router.getCurrentPage());
    }

    @Test
    public void switchPageShouldUpdateCurrentPage() {

        // Switch to a page
        router.switchPage(Pages.DASHBOARD);
        assertEquals(Pages.DASHBOARD, router.getCurrentPage());

        // Switch to a different page
        router.switchPage(Pages.MAP);
        assertEquals(Pages.MAP, router.getCurrentPage());
    }

    @Test
    public void switchPageShouldTriggerCallbacks() {

        // Set up a handler
        AtomicReference<Pages> pageType = new AtomicReference<>();
        router.registerPageChangeCallback(pageType::set);

        // Switch the page
        router.switchPage(Pages.SETTINGS);
        assertEquals(Pages.SETTINGS, pageType.get());
    }

    @Test
    public void shouldSafleyFailToRegisterDuplicateCallback() {

        // Set up a handler
        Consumer<Pages> callback = pageType -> {};
        router.registerPageChangeCallback(callback);

        // Should fail
        assertFalse(router.registerPageChangeCallback(callback));
    }

    @Test
    public void backShouldGoToPreviousPage() {

        // Switch to a page
        router.switchPage(Pages.DASHBOARD);
        router.switchPage(Pages.MAP);

        // Go back
        router.backPage();
        assertEquals(Pages.DASHBOARD, router.getCurrentPage());
    }

}