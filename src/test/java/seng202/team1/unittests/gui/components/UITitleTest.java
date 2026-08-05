package seng202.team1.unittests.gui.components;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;

import static org.junit.jupiter.api.Assertions.*;

class UITitleTest {

    private UITitle title;

    @BeforeEach
    public void init(){

        title = new UITitle("test");

    }

    @Test
    public void titleMustBeCentred(){
        assertEquals(Pos.CENTER, title.getAlignment());
    }
}