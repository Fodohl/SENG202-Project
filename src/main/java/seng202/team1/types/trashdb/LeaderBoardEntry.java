package seng202.team1.types.trashdb;

public record LeaderBoardEntry(

        Integer position,
        Integer user_id,
        String name,
        Integer items_logged,
        Double trees

) {}
