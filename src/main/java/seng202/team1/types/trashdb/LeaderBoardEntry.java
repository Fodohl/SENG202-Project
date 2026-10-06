package seng202.team1.types.trashdb;

import java.time.Instant;

/**
 * TrashAppBackend Type: an entry in the leaderboard
 *
 * @param place The ranking of the user
 * @param userId The id of this user
 * @param userName The name of this user
 * @param logCount How many items of trash this user has logged
 * @param treeDays The amount of tree days this user has saved
 * @param updatedAt When this position was calculated
 */
public record LeaderBoardEntry(

        Integer place,
        Integer userId,
        String userName,
        Integer logCount,
        Double treeDays,
        Instant updatedAt

) {
    public LeaderBoardEntry withPlace(int place) {
        return new LeaderBoardEntry(place, userId, userName, logCount, treeDays, updatedAt);
    }
}
