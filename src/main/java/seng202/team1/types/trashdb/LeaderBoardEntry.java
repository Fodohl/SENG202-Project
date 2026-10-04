package seng202.team1.types.trashdb;

/**
 * TrashAppBackend Type: an entry in the leaderboard
 *
 * @param position The ranking of the user
 * @param user_id The id of this user
 * @param name The name of this user
 * @param items_logged How many items of trash this user has logged
 * @param trees The amount of tree days this user has saved
 */
public record LeaderBoardEntry(

        Integer position,
        Integer user_id,
        String name,
        Integer items_logged,
        Double trees

) {}
