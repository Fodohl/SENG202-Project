package seng202.team1.types;

import seng202.team1.taserver.model.UserResponse;

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
