package seng202.team1.api.trashdb;

import seng202.team1.api.HttpStatus;
import seng202.team1.data.bin.BinDataset;
import seng202.team1.database.DAO.*;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.state.GlobalState;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashdb.CommunityCleanliness;
import seng202.team1.types.trashdb.ResponseSuccess;
import seng202.team1.types.trashdb.TrashDBCleanlinessResponse;
import seng202.team1.types.trashdb.TrashDBFullnessResponse;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.user.Token;
import seng202.team1.types.user.User;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static seng202.team1.api.trashdb.TokenService.EXPIRY_DAYS;
import static seng202.team1.helpers.DateHelpers.toOffsetTime;
import static seng202.team1.state.GlobalState.IS_TEST_MODE;

/**
 * Simulates what would be the trashapp API
 * <p>
 * Notes:
 * - Token would be sent and parsed each call to verify and see which user is request, rather than just once here
 * - Any db cals must not be made to state.db as that is local only
 */
public class TrashDBBackend {

    /// The database connection to the online DB
    private DatabaseManager db;

    /// The DAO responsible for bins
    protected BinDAO binTable;

    /// The DAO responsible for users
    protected UserDAO userTable;

    /// The DAO responsible for trash
    protected TrashLogDAO trashTable;

    /// The DAO responsible for status reports
    protected StatusReportDAO statusReportsTable;

    /// The DAO responsible for community cleanliness
    protected CommunityCleanlinessDAO cleanlinessTable;

    /// The handler for securing user tokens
    private final TokenService tokenService = new TokenService();

    /// The authenticated user making the request
    private User user;

    private static final String testBackendDB = "jdbc:sqlite:./src/test/resources/test_database_be.db";

    /**
     * Constructs a new database backend
     */
    public TrashDBBackend() {

        // Will connect to the online db without a sqlite file
        db = new DatabaseManager(IS_TEST_MODE ? testBackendDB : null);

        binTable = new BinDAO(db);
        userTable = new UserDAO(db);
        trashTable = new TrashLogDAO(db);
        statusReportsTable = new StatusReportDAO(db);
        cleanlinessTable = new CommunityCleanlinessDAO(db);
    }

    /**
     * Entry point that sets up the database on the server
     *
     * @param args N/A
     */
    static void main(String[] args) {

        // Init backend
        TrashDBBackend backend = new TrashDBBackend();
        backend.db.resetDB();

        // Propagate bins
        BinDataset bins = new BinDataset();
        bins.readGeoJSON();
        backend.binTable.addBatch(bins.getBinDataset());
    }

    /**
     * Parse the token give to get a user, clears user if token is null
     *
     * @param token
     */
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

    /**
     * Get the currently authenticated user
     *
     * @return The user or null if none
     */
    private User getUser() {
        return user;
    }

    /**
     * Create a new user account if that email is not already in use
     *
     * @param email       The user's email
     * @param name        The user's full name
     * @param passwordRaw The user's password
     * @return The created user
     */
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

    /**
     * Try log a user in with the given email,password combo
     *
     * @param email       The email of the account to log in to
     * @param passwordRaw The accounts password
     * @return The token to authenticate this user if password is correct, error thrown otherwise
     */
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
                OffsetDateTime.now().plusDays(EXPIRY_DAYS).toInstant()
        );

        return response;

    }

    /**
     * Get the email and name this account has stored on the database
     *
     * @return The user's details if they exist
     */
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

    /**
     * Get the fullness of the bins in the database
     *
     * @param lastUpdated The last time the client checked the fullness, will return the delta dataset since then
     * @return A map of bin-ids to fullness ordinal
     */
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

    /**
     * Save a user's trash log to the db
     *
     * @param log The log to upload
     * @return Success message if the log was uploaded fine, error otherwise
     */
    public ResponseSuccess addUserTrashLog(TrashLog log) {

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

        ResponseSuccess response = new ResponseSuccess("Trash logged successfully");
        return response;
    }

    /**
     * Update the user's information in the database
     *
     * @param name  The new name of the user
     * @param email The new email of the user
     * @return Success message if the user was uploaded fine, error otherwise
     */
    public ResponseSuccess updateUserDetails(String name, String email) {


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

        ResponseSuccess success = new ResponseSuccess("User updated successfully");
        return success;

    }

    /**
     * Change the pasword of the user
     *
     * @param enteredPassword    The user's current password to verify
     * @param enteredPasswordNew The user's new password
     * @return Success message if the user password was changed fine, error otherwise
     */
    public ResponseSuccess updateUserPassword(String enteredPassword, String enteredPasswordNew) {

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


        ResponseSuccess success = new ResponseSuccess("User updated successfully");
        return success;


    }

    /**
     * Delete a user's account
     *
     * @return Success message if the user was deleted fine, error otherwise
     */
    public ResponseSuccess removeUser() {

        // Get the user making the request
        User user = getUser();
        userTable.delete(user.id());

        ResponseSuccess success = new ResponseSuccess("User deleted successfully");
        return success;

    }

    /**
     * Upload a status report to the database
     *
     * @param request The report to upload
     * @return Success message if the report was uploaded fine, error otherwise
     */
    public ResponseSuccess addUserReport(StatusReport request) {

        // Enforce the user id
        User user = getUser();
        StatusReport report = new StatusReport(
                -1,
                request.getBinId(),
                user.id(),
                request.getFullness(),
                request.getIssue(),
                request.getDescription(),
                request.getSubmittedAt(),
                Instant.now()
        );


        // Store
        int id = statusReportsTable.add(report);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save report to database");


        ResponseSuccess success = new ResponseSuccess("Trash logged successfully");
        return success;
    }

    /**
     * Get the amount of trash logged each month for the past 6 months
     *
     * @return The past 6 months of trashlogging totals
     */
    public TrashDBCleanlinessResponse getCollectiveCleanliness() {

        // Get the counts
        ArrayList<CommunityCleanliness> counts = new ArrayList<>(cleanlinessTable.getPastSixMonths());

        int startYear = counts.getFirst().year();
        int startMonth = counts.getFirst().month();

        // Inject data for any months not known
        while (counts.size() != 6) {

            // Go to the prev month
            startMonth -= 1;
            if (startMonth == 0) {
                startMonth = 12;
                startYear -= 1;
            }

            CommunityCleanliness synthetic = new CommunityCleanliness(-1, startYear, startMonth, 0, null);
            counts.addFirst(synthetic);
        }

        return new TrashDBCleanlinessResponse(counts);
    }

    /**
     * Run the worker to calculate the community trash totals for this month (blocking)
     */
    public void runRecountCommunityTrash() {

        CommunityCleanliness current = cleanlinessTable.getThisMonth();

        // Count this month's logs
        OffsetDateTime now = toOffsetTime(Instant.now()).withDayOfMonth(1);
        int count = trashTable.countSince(now.toInstant());

        // Update to the new count
        current = current.withNewCount(count);
        cleanlinessTable.update(current);
    }

    /**
     * Run the worker to calculate the bin fullness (blocking)
     */
    public void runRecomputeFullness() {

        // Recalculate this month's logs
        CommunityCleanliness current = cleanlinessTable.getThisMonth();
        OffsetDateTime now = toOffsetTime(Instant.now()).withDayOfMonth(1);
        int count = trashTable.countSince(now.toInstant());

        // Update to the new count
        current = current.withNewCount(count);
        cleanlinessTable.update(current);

    }
}
