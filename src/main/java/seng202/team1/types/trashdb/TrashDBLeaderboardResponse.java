package seng202.team1.types.trashdb;

import java.util.List;

public record TrashDBLeaderboardResponse (
    List<LeaderBoardEntry> entries,
    Integer nextPage
){}
