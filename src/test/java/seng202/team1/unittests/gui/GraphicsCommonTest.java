package seng202.team1.unittests.gui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.GraphicsCommon;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class GraphicsCommonTest extends TrashAppTest {

     @Test
    void toggleClassShouldBeIdemponent(){
        String className = "active";
        Node testNode = new VBox();

        GraphicsCommon.toggleClass(true, testNode, className);
        GraphicsCommon.toggleClass(true, testNode, className);
        assertEquals(1, testNode.getStyleClass().stream().filter(c -> c.equals(className)).count());

        GraphicsCommon.toggleClass(false, testNode, className);
        GraphicsCommon.toggleClass(false, testNode, className);
        assertFalse(testNode.getStyleClass().contains(className));;
    }

    @Test
    void precentWindowXShouldReturnZeroForInvalidRatios() {
        assertEquals(0, GraphicsCommon.precentWindowX(-0.1));
        assertEquals(0, GraphicsCommon.precentWindowX(1.1));
    }

    @Test
    void precentWindowYShouldReturnZeroForInvalidRatios() {
        assertEquals(0, GraphicsCommon.precentWindowY(-0.1));
        assertEquals(0, GraphicsCommon.precentWindowY(1.1));
    }
}