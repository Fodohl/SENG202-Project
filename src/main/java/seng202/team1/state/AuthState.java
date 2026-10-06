package seng202.team1.state;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.gui.components.BlockableUISet;
import seng202.team1.types.user.Session;
import seng202.team1.types.user.Token;
import seng202.team1.types.user.User;
import seng202.team1.gui.pages.Pages;
import seng202.team1.gui.phases.Phases;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.time.Instant;

import static seng202.team1.TrashApp.GSON;
import static seng202.team1.state.GlobalState.IS_TEST_MODE;

/**
 * Persistently stores the current authentication state, and provides helpers for ensuring correct state values
 */
public class AuthState {

    /// AuthState specific logger
    private static final Logger log = LoggerFactory.getLogger(AuthState.class);

    /// The current logged in session or null if none
    private Session session = null;

    /// The reference to the global state of the app
    private GlobalState state;

    /// The trash app API client to sync the authentication state with
    private TrashDBClient server;

    /**
     * Constructs a new AuthSate
     *
     * @param state The global app state
     */
    public AuthState(GlobalState state) {
        this.state = state;
        this.server = state.getAppClients().getTaServer();

        load();
    }

    /**
     * Get the current auth details and user details
     *
     * @return The auth details or null if none
     */
    public Session getSession() {
        return session;
    }

    /**
     * Update the auth session, propagating it to the backend client
     *
     * @param session The new session
     */
    public void setSession(Session session) {
        this.session = session;
        server.setSession(session);
    }

    /**
     * Log the user into the app, fetching the latest user data and caching it for faster future logins
     *
     * @param email    The email of the account to log in to
     * @param password The accounts password
     * @param frontend The UI to manipulate whilst waiting for the request to complete
     */
    public void login(String email, String password, boolean persist, BlockableUISet frontend) {

        // Authenticate the user
        server.postLogin(email, password, token -> {
            setSession(new Session(null, token.token()));

            // Update the latest user details
            server.getUser(userDetails -> {

                User user = new User(userDetails.id(), userDetails.created(), userDetails.updated(), userDetails.name(), userDetails.email(), null, null);
                setSession(new Session(user, token.token()));

                // Successful login
                tryTriggerLoadSession();
                state.getRouter().switchPhase(Phases.MAIN);

                if(persist)
                    save();

            }, frontend);

        }, frontend);
    }

    /**
     * Create a new user account if that email is not already in use
     *
     * @param userDetails The user's details
     * @param password    The user's password
     * @param frontend    The UI to manipulate whilst waiting for the request to complete
     */
    public void signUp(User userDetails, String password, BlockableUISet frontend) {

        // Create a new user
        server.postUser(userDetails.email(), userDetails.name(), password, r -> {

            // Log into the account
            login(userDetails.email(), password, true, frontend);

        }, frontend);

    }

    /**
     * Reset the users' password
     *
     * @implNote Not implemented due to not having email server
     */
    public void resetPassword(String enteredEmail, String enteredCode, String enteredPassword) {

        // User can now login
        state.getRouter().switchPage(Pages.LOGIN);

    }

    /**
     * Clear the current auth session and prepare for next login
     */
    public void logOut() {

        clear();
        state.getRouter().switchPhase(Phases.AUTH);

    }

    /**
     * Check whether the email is valid, throws error with reason otherwise
     *
     * @param enteredEmail The email to check
     */
    public static void validateEmail(String enteredEmail) {

        if (!EmailValidator.getInstance().isValid(enteredEmail))
            throw new InvalidParameterException("Email is invalid");

    }

    /**
     * Check whether the password is valid, throws error with reason otherwise
     *
     * @param enteredPassword The password to check
     */
    public static void validatePassword(String enteredPassword) {

        if (enteredPassword.length() <= 7)
            throw new InvalidParameterException("Password must be at least 8 characters");

        boolean hasUpperCase = false;
        boolean hasSpecial = false;
        boolean hasNumber = false;

        for (char car : enteredPassword.toCharArray()) {

            if (Character.isUpperCase(car))
                hasUpperCase = true;

            if (Character.isDigit(car))
                hasNumber = true;

            if (!Character.isWhitespace(car) && !Character.isLetterOrDigit(car))
                hasSpecial = true;

        }

        if (!hasUpperCase)
            throw new InvalidParameterException("Password must contain at least one uppercase letter");

        if (!hasSpecial)
            throw new InvalidParameterException("Password must contain at least one symbol");

        if (!hasNumber)
            throw new InvalidParameterException("Password must contain at least one number");
    }

    /**
     * Check whether the name is valid, throws error with reason otherwise
     *
     * @param enteredName The name to check
     */
    public static void validateName(String enteredName) {

        if (enteredName.length() < 3)
            throw new InvalidParameterException("Name must be 3 or more characters");

        if (enteredName.length() > 15)
            throw new InvalidParameterException("Name must be 15 or less characters");

    }

    /**
     * Check whether the code is valid, throws error with reason otherwise
     *
     * @param enteredCode The code to check
     */
    public static Integer validateCode(String enteredCode) {

        if (enteredCode.length() != 6)
            throw new InvalidParameterException("Code must be 6 digits");

        try {
            Integer code = Integer.parseInt(enteredCode);
            return code;

        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Code must be a valid number");
        }
    }

    /**
     * Returns the default location for the user's saved session.
     *
     * @return default JSON file location
     *
     */
    public static Path defaultDataPath() {
        return Path.of(
                System.getProperty("user.home"),
                ".trashapp",
                "session.json"
        );
    }

    /**
     * Persist the current auth session to disk
     */
    private void save() {

        // No saved session
        if (session == null || session.userDetails() == null || session.token() == null)
            return;

        try {

            // Make sure the trashapp config dir exists
            Files.createDirectories(defaultDataPath().getParent());

            // Write the session
            try (FileWriter writer = new FileWriter(defaultDataPath().toFile())) {
                GSON.toJson(session, writer);
            }

            log.info("Session saved successfully");

        } catch (IOException e) {
            log.error("Failed to save session", e);
        }
    }

    /**
     * Load a saved session from disk
     */
    private void load() {

        // No saved session
        if (!Files.exists(defaultDataPath()))
            return;

        try (var reader = Files.newBufferedReader(defaultDataPath())) {

            // Read the session, forcing propagation to backend
            setSession(GSON.fromJson(reader, Session.class));

            if (this.session != null && this.session.token() != null) {
                log.info("Restored session for user: {}", session.userDetails() != null ? session.userDetails().email() : "Unknown");
                tryTriggerLoadSession();
            }

        } catch (Exception e) {

            log.error("Failed to load session", e);
            clear();
        }

    }

    /**
     * Delete a saved session if it exists (fails safely)
     */
    private void clear() {

        try {
            Files.deleteIfExists(defaultDataPath());

        } catch (IOException e) {
            log.info("Failed to delete session", e);

        } finally {
            setSession(null);
        }
    }

    /**
     * Trigger run tasks that require user auth
     */
    private void onSessionLoaded() {

        // Now that user is authenticated, database can identify them and thus data can be fetched
        state.getUiContext().fetchData();
        state.getDataService().downloadMissingData();

        // Now that have latest data
        state.getAppTasks().runWorkers();

        // Begin processing queued uploads
        state.sync();

    }

    /**
     * Update user details and save it to session
     *
     * @param enteredName       The user's email
     * @param enteredEmail        The user's full name
     */
    public void updateUser(String enteredName, String enteredEmail) {

        User oldUser = session.userDetails();
        User newUserDetails = new User(
                oldUser.id(),

                oldUser.created(),
                Instant.now(),

                enteredName,
                enteredEmail,

                null,
                null
        );

        setSession(session.withUser(newUserDetails));
        save();

    }

    /**
     * Try trigger a session load event, if there is a session to trigger it on
     */
    public void tryTriggerLoadSession() {

        // No session
        if (session == null || session.token() == null)
            return;

        // UI not ready
        if (state.getRouter() == null)
            return;

        onSessionLoaded();
    }
}
