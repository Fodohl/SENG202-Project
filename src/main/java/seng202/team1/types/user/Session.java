package seng202.team1.types.user;

import java.time.Instant;

/**
 * Represents a user authentication session
 *
 * @param userDetails The details of the user
 * @param token The token that authenticates the user
 * @param lastLogin When the last login to this account occured on this device
 */
public record Session(

        User userDetails,
        String token,
        Instant lastLogin

) {

    /**
     * Construct a new Session logged in now
     *
     * @param userDetails The details of the user
     * @param token The token that authenticates the user
     */
    public Session(User userDetails, String token) {
        this(userDetails, token, Instant.now());
    }

    /**
     * Clones this session to have updated user details
     *
     * @param newUserDetails The updated details
     * @return The updated session
     */
    public Session withUser(User newUserDetails) {
        return new Session(
                newUserDetails,
                token,
                lastLogin
        );
    }
}
