package seng202.team1.state;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.w3c.dom.stylesheets.LinkStyle;
import seng202.team1.api.tasever.TAServerClient;
import seng202.team1.taserver.model.TokenResponse;
import seng202.team1.taserver.model.UserResponse;
import seng202.team1.types.user.Session;
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

public class AuthState {

    private static final Logger log = LoggerFactory.getLogger(AuthState.class);

    private Session session = null;

    private GlobalState state;

    private TAServerClient server;

    public AuthState(GlobalState state) {
        this.state = state;
        this.server = state.getTaServer();

        load();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        server.setSession(session);
    }

    public void login(String email, String password) {

        // Authenticate the user
        TokenResponse token = server.postLogin(email, password, r -> {}).join();
        setSession(new Session(null, token.getToken()));

        // Updated the user details
        UserResponse userDetails = server.getUser(r -> {}).join();
        User user = new User(userDetails.getId(), userDetails.getCreated().toInstant(), userDetails.getUpdated().toInstant(), userDetails.getName(), userDetails.getEmail(), null, null);
        setSession(new Session(user, token.getToken()));

        // Successful login
        state.getRouter().switchPhase(Phases.MAIN);
        save();
    }

    public void signUp(User userDetails, String password) {

        // Create a new user
        server.postUser(userDetails.email(), userDetails.name(), password, r -> {
            System.out.println(r);
        }).join();

        // Log into the account
        login(userDetails.email(), password);

    }

    public void resetPassword(String enteredEmail, String enteredCode, String enteredPassword) {

        // User can now login
        state.getRouter().switchPage(Pages.LOGIN);

    }

    public void logOut() {

        clear();
        state.getRouter().switchPhase(Phases.AUTH);

    }

    public void validateEmail(String enteredEmail) {

        if (!EmailValidator.getInstance().isValid(enteredEmail))
            throw new InvalidParameterException("Email is invalid");

    }

    public void validatePassword(String enteredPassword) {

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

    public void validateName(String enteredName) {

        if (enteredName.length() < 3)
            throw new InvalidParameterException("Name must be 3 or more characters");

        if (enteredName.length() > 15)
            throw new InvalidParameterException("Name must be 15 or less characters");

    }

    public Integer validateCode(String enteredCode) {

        if (enteredCode.length() != 6)
            throw new InvalidParameterException("Code must e 6 digits");

        try {
            Integer code = Integer.parseInt(enteredCode);
            return code;

        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Code must be a valid number");
        }
    }

    /**
     * Returns the default location for the user's saved trash history.
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

    private void load() {

        // No saved session
        if (!Files.exists(defaultDataPath()))
            return;

        try (var reader = Files.newBufferedReader(defaultDataPath())) {

            // Read the session, forcing propagation to backend
            setSession(GSON.fromJson(reader, Session.class));

            if (this.session != null && this.session.token() != null)
                log.info("Restored session for user: {}", session.userDetails() != null ? session.userDetails().email() : "Unknown");


        } catch (Exception e) {

            log.error("Failed to load session", e);
            clear();
        }

    }

    private void clear() {

        try {
            Files.deleteIfExists(defaultDataPath());

        } catch (IOException e) {
            log.info("Failed to delete session", e);

        } finally {
            setSession(null);
        }
    }

    private void onSessionLoaded(){

        // fetch app data

        // begin processing queued uploads

    }
}
