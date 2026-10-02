package seng202.team1.types.user;

import java.time.Instant;

public record Session(

        User userDetails,
        String token,
        Instant lastLogin

) {
    public Session(User userDetails, String token) {
        this(userDetails, token, Instant.now());
    }

    public Session withUser(User newUserDetails) {
        return new Session(
                newUserDetails,
                token,
                lastLogin
        );
    }
}
