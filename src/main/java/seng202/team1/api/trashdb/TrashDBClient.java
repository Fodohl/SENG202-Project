package seng202.team1.api.trashdb;

import seng202.team1.api.ApiBackend;
import seng202.team1.database.DAO.CommunityCleanlinessDAO;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.gui.components.BlockableUISet;
import seng202.team1.helpers.DateHelpers;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashdb.*;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.user.Session;
import seng202.team1.types.user.Token;
import seng202.team1.types.user.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;
import static seng202.team1.gui.GraphicsCommon.NO_LOADING_UI;
import static seng202.team1.helpers.DateHelpers.toOffsetTime;

/**
 * API client to abstract async communication with the trash app's database
 * <p>
 * Note: simulates communication with what would be the trash app's API via the {@link  TrashDBBackend}
 */
public class TrashDBClient extends ApiBackend {

    /// How many leaderboard entries to fetch
    private final Integer LIMIT_LEADERBOARD_ENTRIES = 3;

    /// The user session to use for authentication
    private Session session = null;

    /// The backend to simulate the API and handle interfacing with the database
    private static TrashDBBackend backend = new TrashDBBackend();

    /**
     * Construct a new TrashDBClient
     */
    public TrashDBClient() {
        super("null");
    }

    /**
     * Set the current session
     *
     * @param session The session to use
     */
    public void setSession(Session session) {

        this.session = session;

        backend.setToken(session != null ? session.token() : "");
    }

    /**
     * Create a new user account if that email is not already in use
     *
     * @param email       The user's email
     * @param name        The user's full name
     * @param passwordRaw The user's password
     * @param callback    The handler to run when the creation completes
     * @param frontend    The UI to manipulate whilst waiting for the request to complete
     */
    public void postUser(String email, String name, String passwordRaw, Consumer<User> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.addNewUser(email, name, passwordRaw), callback, frontend);
    }


    /**
     * Try log a user in with the given email,password combo
     *
     * @param email       The email of the account to log in to
     * @param passwordRaw The accounts password
     * @param callback    The handler to run when the request completes
     * @param frontend    The UI to manipulate whilst waiting for the request to complete
     */
    public void postLogin(String email, String passwordRaw, Consumer<Token> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.logUserIn(email, passwordRaw), callback, frontend);
    }

    /**
     * Get the email and name this account has stored on the database
     *
     * @param callback The handler to run when the request completes
     * @param frontend The UI to manipulate whilst waiting for the request to complete
     */
    public void getUser(Consumer<User> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.getUserDetails(), callback, frontend);
    }

    /**
     * Get the fullness of the bins in the database
     *
     * @param callback The handler to run when the request completes
     * @return The async request
     */
    public CompletableFuture<TrashDBFullnessResponse> getBinFullness(Consumer<TrashDBFullnessResponse> callback) {

        OffsetDateTime updated = session == null ? null : DateHelpers.toOffsetTime(session.lastLogin());
        return sendAsync(() -> backend.getBinsFullness(updated), callback, NO_LOADING_UI);
    }

    /**
     * Get the leaderboard entries
     *
     * @param callback The handler to run when the request completes
     */
    public void getLeaderboard(Consumer<TrashDBLeaderboardResponse> callback) {

        String mockJson = """
                    {
                      "entries": [
                        {
                          "position": 1,
                          "user_id": 101,
                          "name": "Archie G",
                          "items_logged": 450,
                          "trees": 1.5
                        },
                        {
                          "position": 2,
                          "user_id": 102,
                          "name": "Bob R",
                          "items_logged": 422,
                          "trees": 1.4
                        },
                        {
                          "position": 3,
                          "user_id": 103,
                          "name": "Tony D",
                          "items_logged": 420,
                          "trees": 1.4
                        },
                        {
                          "position": 4,
                          "user_id": 104,
                          "name": "Mike M",
                          "items_logged": 400,
                          "trees": 1.3
                        },
                        {
                          "position": 5,
                          "user_id": 105,
                          "name": "Ryan W",
                          "items_logged": 399,
                          "trees": 1.3
                        },
                        {
                          "position": 6,
                          "user_id": 106,
                          "name": "Carter C",
                          "items_logged": 350,
                          "trees": 1.2
                        },
                        {
                          "position": 7,
                          "user_id": 107,
                          "name": "Steve L",
                          "items_logged": 100,
                          "trees": 1.0
                        },
                          {
                          "position": 8,
                          "user_id": 107,
                          "name": "Steve L",
                          "items_logged": 100,
                          "trees": 1.0
                        }
                      ],
                      "nextPage": null
                    }
                """;

        callback.accept(GSON.fromJson(mockJson, TrashDBLeaderboardResponse.class));

//
//        Map<String, String> params = Map.of("page", "0",
//                                            "limit", LIMIT_LEADERBOARD_ENTRIES.toString(),
//                                            "add_user_row", "true"
//                                            );
//
//        GET("leaderboard", params, json -> {
//            try {
//                callback.accept(GSON.fromJson(json, TrashDBLeaderboardResponse.class));
//
//            } catch (Exception e) {
//                log.warn(String.format("Failed to parse PhotonLocationResponse: %s", e.getMessage()));
//            }
//
//        });
    }

    /**
     * Save a user's trash log to the db
     *
     * @param log      The log to upload
     * @param callback The handler to run when the request completes
     * @return The async request
     */
    public CompletableFuture<ResponseSuccess> postLog(TrashLog log, Consumer<ResponseSuccess> callback) {

        return sendAsync(() -> backend.addUserTrashLog(log), callback, NO_LOADING_UI);
    }

    /**
     * Update the user's information in the database
     *
     * @param name     The new name of the user
     * @param email    The new email of the user
     * @param callback The handler to run when the request completes
     * @param frontend The UI to manipulate whilst waiting for the request to complete
     */
    public void updateUser(String name, String email, Consumer<ResponseSuccess> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.updateUserDetails(name, email), callback, frontend);

    }

    /**
     * Change the pasword of the user
     *
     * @param enteredPassword    The user's current password to verify
     * @param enteredPasswordNew The user's new password
     * @param callback           The handler to run when the request completes
     * @param frontend           The UI to manipulate whilst waiting for the request to complete
     */
    public void updatePassword(String enteredPassword, String enteredPasswordNew, Consumer<ResponseSuccess> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.updateUserPassword(enteredPassword, enteredPasswordNew), callback, frontend);

    }

    /**
     * Delete a user's account
     *
     * @param callback The handler to run when the request completes
     * @param frontend The UI to manipulate whilst waiting for the request to complete
     */
    public void deleteUser(Consumer<ResponseSuccess> callback, BlockableUISet frontend) {
        sendAsync(() -> backend.removeUser(), callback, frontend);
    }

    /**
     * Upload a status report to the database
     *
     * @param report The report to upload
     * @param callback The handler to run when the request completes
     * @return The async request
     */
    public CompletableFuture<ResponseSuccess> postReport(StatusReport report, Consumer<ResponseSuccess> callback) {

        return sendAsync(() -> backend.addUserReport(report), callback, NO_LOADING_UI);

    }

    /**
     * Get the amount of trash logged each month for the past 6 months
     *
     * @param callback The handler to run when the request completes
     */
    public void getCommunityCleanliness(Consumer<TrashDBCleanlinessResponse> callback) {

        sendAsync(() -> backend.getCollectiveCleanliness(), callback, NO_LOADING_UI);

    }

    /**
     * Run the worker to calculate the community trash totals for this month (blocking)
     */
    public void runRecountCommunityTrash() {
        backend.runRecountCommunityTrash();
    }

    /**
     * Run the worker to calculate the bin fullness (blocking)
     */
    public void runRecomputeFullness() {
        backend.runRecomputeFullness();
    }
}