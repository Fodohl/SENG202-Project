package seng202.team1.unittests.gui.core;

import javafx.scene.control.ScrollPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.core.CoreUI;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class CoreUITest extends TrashAppTest {

    private CoreUI ui;

    @BeforeEach
    public void init(){
        ui = new CoreUI(windowManager);
        ui.rerender();
    }

    @Test
    public void swapShouldSetPageContents(){

        // Swap a test node in
        UITitle newNode = new UITitle("test");
        ui.swap(newNode);

        // Get the page contents
        ScrollPane contents = (ScrollPane)ui.getPage().getCenter();
        assertEquals(newNode, contents.getContent());
    }

}