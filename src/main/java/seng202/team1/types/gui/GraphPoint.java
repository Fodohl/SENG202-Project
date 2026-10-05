package seng202.team1.types.gui;

import seng202.team1.types.trashdb.CommunityCleanliness;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public record GraphPoint(
        String name,
        int value
) {

    public static GraphPoint fromCCleanliness(CommunityCleanliness item) {

        String monthName = Month.of(item.month()).getDisplayName(TextStyle.SHORT, Locale.ROOT);
        return new GraphPoint(monthName, item.log_count());
    }
}