package seng202.team1.api.trashdb;

import java.time.Instant;

public record User(

        Integer _id,
        Instant created,
        Instant updated,

        String name,
        String email,
        String passwordHash,
        Integer resetCode
){}
