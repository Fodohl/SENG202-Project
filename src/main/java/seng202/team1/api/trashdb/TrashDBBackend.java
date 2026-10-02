package seng202.team1.api.trashdb;

import seng202.team1.api.HttpStatus;
import seng202.team1.data.bin.BinDataset;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.database.DAO.UserDAO;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.state.GlobalState;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.trashdb.ResponseMessage;
import seng202.team1.types.trashdb.TrashDBFullnessResponse;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;
import seng202.team1.types.user.Token;
import seng202.team1.types.user.User;

import java.security.InvalidParameterException;
import java.time.Instant;
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

    //@TODO: c queue upload status
    //@TODO: s worked calculate fullness
    //@TODO: s worker calculate leaderboard
    //@TODO: change password / details
    //@TODO: delete account
    //@TODO: download my stuff from db

    private DatabaseManager db;
    private BinDAO binTable;
    private UserDAO userTable;
    private TrashLogDAO trashTable;

    private final TokenService tokenService = new TokenService();

    private User user;

    public TrashDBBackend() {

        // Will connect to the db without a sqlite file
        db = new DatabaseManager(null);

        binTable = new BinDAO(db);
        userTable = new UserDAO(db);
        trashTable = new TrashLogDAO(db);

    }


    static void main(String[] args) {

        // Init backend
        TrashDBBackend backend = new TrashDBBackend();
        backend.db.resetDB();

        // Propagate bins
        BinDataset bins = new BinDataset();
        bins.readGeoJSON();
        backend.binTable.addBatch(bins.getBinDataset());
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
        User user = getUser();

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
        if (token.isEmpty()) {
            user = null;
            return;
        }

        // Parse the token
        Integer userID = Math.toIntExact(tokenService.parseToken(token));

        // Invalid token
        if (userID == -1)
            throw new InvalidParameterException("Token malformed or not signed");

        user = userTable.getOne(userID);

    }

    public ResponseMessage addUserTrashLog(TrashLog log) {

        // Enforce the user id
        User user = getUser();
        TrashLog cleanedLog = new TrashLog(
                -1,
                log.createdAt(),
                log.disposedAt(),
                Instant.now(),
                user.id(),
                log.binID(),
                log.trashType(),
                log.trashWeight()
        );


        // Store
        int id = trashTable.add(cleanedLog);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save log to database");

        ResponseMessage response = new ResponseMessage("Trash logged successfully");
        return response;
    }

    private User getUser() {
        return user;
    }

    public ResponseMessage updateUserDetails(String name, String email) {


        // Get the user making the request
        User user = getUser();

        // Cant update to this email its taken
        boolean newEmail = !user.email().equals(email);
        if (newEmail && userTable.getByEmail(email) != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email already exists");

        // Update the user (assumed logged in so clear reset code)
        User cleanedUser = new User(
                user.id(),

                user.created(),
                Instant.now(),

                name,
                email,

                user.passwordHash(),
                null
        );
        userTable.update(cleanedUser);

        ResponseMessage success = new ResponseMessage("User updated successfully");
        return success;

    }

    public ResponseMessage updateUserPassword(String enteredPassword, String enteredPasswordNew) {

        // Get the user making the request
        User user = getUser();

        // Password wrong
        if (!PasswordHasher.verify(enteredPassword, user.passwordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");

        // Update the user (assumed logged in so clear reset code)
        User cleanedUser = new User(
                user.id(),

                user.created(),
                Instant.now(),

                user.name(),
                user.email(),

                PasswordHasher.hash(enteredPasswordNew),
                null
        );
        userTable.update(cleanedUser);


        ResponseMessage success = new ResponseMessage("User updated successfully");
        return success;


    }

    public ResponseMessage removeUser() {

        // Get the user making the request
        User user = getUser();
        userTable.delete(user.id());

        ResponseMessage success = new ResponseMessage("User deleted successfully");
        return success;

    }
}
