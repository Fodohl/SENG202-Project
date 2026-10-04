package seng202.team1.types.user;

import java.time.Instant;

/**
 * Represents a authentication token
 *
 * @param token The auth token
 * @param expires When the token expires
 */
public record Token (

        String token,
        Instant expires

){ }
