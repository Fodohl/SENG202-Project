package seng202.team1.types.user;

import java.time.Instant;

/**
 * Represents a user of the app
 *
 * @param id The user's ID
 * @param created When this account was created
 * @param updated When this account was last updated
 * @param name The full name of this user
 * @param email The email of this user
 * @param passwordHash The hashed password of this user, null if not running on the backend
 * @param resetCode The reset code for the user's password, null if not running on the backends
 */
public record User(

        Integer id,
        Instant created,
        Instant updated,

        String name,
        String email,
        String passwordHash,
        Integer resetCode
){}
