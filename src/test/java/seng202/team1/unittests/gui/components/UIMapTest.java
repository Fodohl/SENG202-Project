package seng202.team1.unittests.gui.components;

import org.junit.jupiter.api.BeforeEach;
import seng202.team1.gui.components.UIMap;
import seng202.team1.unittests.TrashAppTest;

class UIMapTest extends TrashAppTest {

    private UIMap map;

    @BeforeEach
    public void init(){
        this.map = new UIMap();
    }

}