package seng202.team1.unittests.helpers;

import org.junit.jupiter.api.Test;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class StringHelpersTest extends TrashAppTest {

    private static String VALID_TITLE = "Test";

    @Test
    public void upperShouldHaveCorrectLowercases() {
        assertEquals(VALID_TITLE, StringHelpers.toTitleCase(VALID_TITLE.toUpperCase()));
    }

    @Test
    public void lowerShouldHaveCorrectUppercase() {
        assertEquals(VALID_TITLE, StringHelpers.toTitleCase(VALID_TITLE.toLowerCase()));
    }

    @Test
    public void alreadyTitleShouldRemainSame() {
        assertEquals(VALID_TITLE, StringHelpers.toTitleCase(VALID_TITLE));
    }

    @Test
    public void emptyShouldBeHandledEdgeCase() {
        assertDoesNotThrow(() -> StringHelpers.toTitleCase(""));
    }

    @Test
    void enumToTitleShouldReplaceSpaces() {
        assertEquals("Page No Execute", StringHelpers.enumNameToTitle("PAGE_NO_EXECUTE"));
    }

    @Test
    void enumToTitleShouldCaptiliseEachWord() {
        assertEquals("Journal Device", StringHelpers.enumNameToTitle("JOURNAL_DEVICE"));
    }

    @Test
    void dateToLocaleShouldBeNullSafe() {
        Instant date = null;
        assertEquals("Unknown Date", StringHelpers.toLocaleString(date));
    }

    @Test
    void roundToNearestShouldBeInt() {
        String result = StringHelpers.roundToNearest(12.4, 5);
        assertDoesNotThrow(() -> Integer.parseInt(result));
    }

    @Test
    void roundToNearestShouldRoundCorrectly() {
        assertEquals("10", StringHelpers.roundToNearest(12.0, 5));
        assertEquals("15", StringHelpers.roundToNearest(13.6, 5));
        assertEquals("100", StringHelpers.roundToNearest(98.0, 10));
    }
}