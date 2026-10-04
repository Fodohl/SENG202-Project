package seng202.team1.types.trashdb;

import java.util.List;

/**
 * TrashAppBackend Type: Page of the leaderboard
 *
 * @param entries The list of entries for this page
 * @param nextPage The next page of the leaderboard, null if no next page
 */
public record TrashDBLeaderboardResponse (
    List<LeaderBoardEntry> entries,
    Integer nextPage
){}
