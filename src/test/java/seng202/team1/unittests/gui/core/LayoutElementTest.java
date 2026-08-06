package seng202.team1.unittests.gui.core;

import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.core.CoreUI;
import seng202.team1.gui.core.LayoutElement;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class LayoutElementTest extends TrashAppTest {

    LayoutElement layoutElement = null;

    @BeforeEach
    public void init(){
        layoutElement = new CoreUI(windowManager);
    }

    @Test
    public void getRenderedShouldNeverReturnNull(){

        assertNotNull(layoutElement.getRendered());
    }

    @Test
    public void rerenderShouldRefreshContent() {

        // Get initial rendered node
        Parent firstRender = layoutElement.getRendered();
        assertNotNull(firstRender);

        // Force a rerender
        layoutElement.rerender();
        Parent secondRender = layoutElement.getRendered();

        assertNotEquals(firstRender, secondRender);
    }
}