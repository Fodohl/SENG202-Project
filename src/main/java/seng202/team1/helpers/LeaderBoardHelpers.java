package seng202.team1.helpers;

import seng202.team1.types.trashdb.LeaderBoardEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardHelpers {


    public static List<LeaderBoardEntry> toRankedOrder(List<LeaderBoardEntry> data){

        // Get all in highest to lowest tree days
         List<LeaderBoardEntry> sorted = data.stream().sorted(Comparator.comparing(entry -> entry.treeDays()))
                                                      .toList().reversed();

        List<LeaderBoardEntry> entries = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++)
            entries.add(sorted.get(i).withPlace(i + 1));

        return entries;
    }
}
