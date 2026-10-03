package seng202.team1.unittests.gui.core;

import org.junit.jupiter.api.Test;
import seng202.team1.gui.pages.Pages;
import seng202.team1.gui.phases.Phases;
import seng202.team1.unittests.TrashAppTest;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest extends TrashAppTest {


    @Test
    public void entryPageShouldBeStarting() {
        router.switchPhase(Phases.PREFLIGHT);
        assertEquals(Pages.STARTING, router.getCurrentPage());
    }

    @Test
    public void switchPageShouldUpdateCurrentPage() {

        router.switchPhase(Phases.MAIN);

        // Switch to a page
        router.switchPage(Pages.HOME);
        assertEquals(Pages.HOME, router.getCurrentPage());

        // Switch to a different page
        router.switchPage(Pages.MAP);
        assertEquals(Pages.MAP, router.getCurrentPage());
    }

    @Test
    public void switchPageShouldTriggerCallbacks() {

        router.switchPhase(Phases.MAIN);

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

        router.switchPhase(Phases.MAIN);

        // Switch to a page
        router.switchPage(Pages.HOME);
        router.switchPage(Pages.MAP);

        // Go back
        router.backPage();
        assertEquals(Pages.HOME, router.getCurrentPage());
    }

}