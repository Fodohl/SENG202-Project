package seng202.team1.types.user;

import java.time.Instant;

public record User(

        Integer id,
        Instant created,
        Instant updated,

        String name,
        String email,
        String passwordHash,
        Integer resetCode
){}
