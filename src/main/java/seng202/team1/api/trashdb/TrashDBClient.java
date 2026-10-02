package seng202.team1.api.trashdb;

import com.steadystate.css.util.LangUtils;
import seng202.team1.api.ApiBackend;
import seng202.team1.gui.components.BlockableUISet;
import seng202.team1.helpers.DateHelpers;
import seng202.team1.state.GlobalState;
import seng202.team1.types.trashdb.ResponseMessage;
import seng202.team1.types.trashdb.TrashDBFullnessResponse;
import seng202.team1.types.trashdb.TrashDBLeaderboardResponse;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.user.Session;
import seng202.team1.types.user.Token;
import seng202.team1.types.user.User;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;
import static seng202.team1.gui.GraphicsCommon.NO_LOADING_UI;
import static seng202.team1.helpers.DateHelpers.toOffsetTime;

public class TrashDBClient extends ApiBackend {

    private final Integer LIMIT_LEADERBOARD_ENTRIES = 3;

    private Session session = null;

    private static TrashDBBackend backend = new TrashDBBackend();

    public TrashDBClient() {
        super("null");
    }

    public void setSession(Session session) {

        this.session = session;

        backend.setToken(session != null ? session.token() : "");
    }

    public void postUser(String email, String name, String passwordRaw, Consumer<User> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.addNewUser(email, name, passwordRaw), callback, frontend);
    }


    public void postLogin(String email, String passwordRaw, Consumer<Token> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.logUserIn(email, passwordRaw), callback, frontend);
    }

    public void getUser(Consumer<User> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.getUserDetails(), callback, frontend);
    }

    public CompletableFuture<TrashDBFullnessResponse> getBinFullness(Consumer<TrashDBFullnessResponse> callback) {

        OffsetDateTime updated = session == null ? null : DateHelpers.toOffsetTime(session.lastLogin());
        return sendAsync(() -> backend.getBinsFullness(updated), callback, NO_LOADING_UI);
    }

    public void getCommunity(Consumer<TrashDBLeaderboardResponse> callback) {

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

    public CompletableFuture<ResponseMessage> postLog(TrashLog log, Consumer<ResponseMessage> callback) {

        return sendAsync(() -> backend.addUserTrashLog(log), callback, NO_LOADING_UI);
    }

    public void updateUser(String name, String email, Consumer<ResponseMessage> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.updateUserDetails(name, email), callback, frontend);

    }

    public void updatePassword(String enteredPassword, String enteredPasswordNew, Consumer<ResponseMessage> callback, BlockableUISet frontend) {

        sendAsync(() -> backend.updateUserPassword(enteredPassword, enteredPasswordNew), callback, frontend);

    }

    public void deleteUser(Consumer<ResponseMessage> callback, BlockableUISet frontend) {
        sendAsync(() -> backend.removeUser(), callback, frontend);
    }
}