package seng202.team1.types.user;

import java.time.Instant;

public record Token (

        String token,
        Instant expires

){ }
