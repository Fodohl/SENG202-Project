package seng202.team1.api.trashdb;

import seng202.team1.api.HttpStatus;
import seng202.team1.data.bin.BinDataset;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.database.DAO.UserDAO;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.state.GlobalState;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.trashdb.TrashDBFullnessResponse;
import seng202.team1.types.user.Token;
import seng202.team1.types.user.User;

import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

import static seng202.team1.helpers.DateHelpers.toOffsetTime;

/**
 * Simulates API
 * <p>
 * Notes:
 * - Token would be sent and parsed each call to verify and see which user is request, rather than just once here
 */
public class TrashDBBackend {

    //@TODO: c queue upload trash
    //@TODO: c queue upload status
    //@TODO: s worked calculate fullness
    //@TODO: s worker calculate leaderboard
    //@TODO: change password / details
    //@TODO: delete account
    //@TODO: download my stuff from db

    private DatabaseManager db;
    private BinDAO binTable;
    private UserDAO userTable;

    private final TokenService tokenService = new TokenService();

    private Integer userID;

    public TrashDBBackend() {

        // Will connect to the db without a sqlite file
        db = new DatabaseManager(null);

        binTable = new BinDAO(db);
        userTable = new UserDAO(db);

    }

    public User addNewUser(String email, String name, String passwordRaw) {


        User user = new User(null, null, null, name, email, PasswordHasher.hash(passwordRaw), null);

        try {

            // Try to add the user to the db
            if (userTable.add(user) == -1)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user to database");

            return user;

        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email already exists");
        }
    }

    public Token logUserIn(String email, String passwordRaw) {

        // User with that email must exist
        User user = userTable.getByEmail(email);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");

        if (!PasswordHasher.verify(passwordRaw, user.passwordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");

        // Authenticate the user
        Token response = new Token(
                tokenService.signToken(user.id()),
                OffsetDateTime.now().plusDays(30).toInstant()
        );

        return response;

    }

    public User getUserDetails() {


        // Get the user
        User user = userTable.getOne(userID);

        if (user == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists");

        // Clean output to hide fields
        User response = new User(
                user.id(),
                user.created(),
                user.updated(),

                user.name(),
                user.email(),

                null,
                null
        );
        return response;
    }

    public TrashDBFullnessResponse getBinsFullness(OffsetDateTime lastUpdated) {


        List<Bin> bins = binTable.getBinsAfter(lastUpdated == null ? null : lastUpdated.toInstant());

        // Map the bin to it's fullness
        HashMap<Integer, Integer> fullness = new HashMap<>();
        bins.forEach(bin -> {
            fullness.put(bin.getID(), bin.getFullness().ordinal());
        });

        TrashDBFullnessResponse response = new TrashDBFullnessResponse(fullness);
        return response;
    }


    public void setToken(String token) {

        // Log out request
        if(token == null) {
            userID = null;
            return;
        }

        // Parse the token
        userID = Math.toIntExact(tokenService.parseToken(token));

        // Invalid token
        if (userID == -1)
            throw new InvalidParameterException("Token malformed or not signed");

    }
}
