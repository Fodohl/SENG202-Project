package seng202.team1.unittests.common;

import org.junit.jupiter.api.Test;
import seng202.team1.common.StringHelpers;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class StringHelpersTest extends TrashAppTest {

    private static String VALID_TITLE = "Test";

    @Test
    public void upperShouldHaveCorrectLowercases(){
        assertEquals(VALID_TITLE, StringHelpers.toTitleCase(VALID_TITLE.toUpperCase()));
    }

    @Test
    public void lowerShouldHaveCorrectUppercase(){
        assertEquals(VALID_TITLE, StringHelpers.toTitleCase(VALID_TITLE.toLowerCase()));
    }

    @Test
    public void alreadyTitleShouldRemainSame(){
        assertEquals(VALID_TITLE, StringHelpers.toTitleCase(VALID_TITLE));
    }

    @Test
    public void emptyShouldBeHandledEdgeCase(){
        assertDoesNotThrow(() -> StringHelpers.toTitleCase(""));
    }
}