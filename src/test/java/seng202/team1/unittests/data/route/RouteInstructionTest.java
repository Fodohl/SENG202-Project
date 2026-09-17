package seng202.team1.unittests.data.route;

import org.junit.jupiter.api.Test;
import seng202.team1.data.route.RouteInstruction;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class RouteInstructionTest extends TrashAppTest {

    @Test
    void shouldReplaceSpaceDelimitersWithSpaces() {

        // Leaflet/JS turns space into '+'
        RouteInstruction instruction = new RouteInstruction("Turn+Left", "", 0, 0);

        assertEquals("Turn Left", instruction.getText());
    }

}