package seng202.team1.unittests.gui.core;

import org.junit.jupiter.api.Test;
import seng202.team1.gui.GraphicsCommon;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;
import static seng202.team1.gui.GraphicsCommon.*;

class GraphicsCommonTest extends TrashAppTest {

    @Test
    public void widthShouldRoundDown(){
        double percent = 1.0 / 3.0;
        assertEquals(WINDOW_WIDTH/3, GraphicsCommon.precentWindowX(percent));
    }


    @Test
    public void heightShouldRoundDown(){
        double percent = 1.0 / 3.0;
        assertEquals(WINDOW_HEIGHT/3, GraphicsCommon.precentWindowY(percent));
    }


    @Test
    public void widthShouldBeZeroIfOutOfBounds(){

        assertEquals(0, GraphicsCommon.precentWindowX(-1));
        assertEquals(0, GraphicsCommon.precentWindowX(20));

    }


    @Test
    public void heightShouldBeZeroIfOutOfBounds(){


        assertEquals(0, GraphicsCommon.precentWindowY(-1));
        assertEquals(0, GraphicsCommon.precentWindowY(20));


    }

}