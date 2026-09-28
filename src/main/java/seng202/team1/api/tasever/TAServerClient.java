package seng202.team1.api.tasever;

import seng202.team1.api.ApiBackend;
import seng202.team1.helpers.DateHelpers;
import seng202.team1.taserver.api.AuthApi;
import seng202.team1.taserver.api.BinApi;
import seng202.team1.taserver.api.DefaultApi;
import seng202.team1.taserver.api.UserApi;
import seng202.team1.taserver.client.ApiClient;
import seng202.team1.taserver.model.*;
import seng202.team1.types.user.Session;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;

public class TAServerClient extends ApiBackend {

    private final Integer LIMIT_LEADERBOARD_ENTRIES = 3;

    private ApiClient client = new ApiClient();

    private Session session = null;

    private DefaultApi defaultApiClient;
    private UserApi userApiClient;
    private AuthApi authApiClient;
    private BinApi binApiClient;

    public TAServerClient(){
        super("null");

        // If running with a dev server use that
        String url = System.getProperty("trashapp.api.url");
        if(url != null)
            client.updateBaseUri(url);

        log.info("Connecting to API base path: {}", client.getBaseUri());
        initApis();

    }

    public CompletableFuture<Heartbeat200Response> getHeartbeat(Consumer<Heartbeat200Response> callback){
        return sendAsync(() -> defaultApiClient.heartbeat(), callback, null, null);
    }

    public CompletableFuture<UserResponse> postUser(String email, String name, String passwordRaw, Consumer<UserResponse> callback){

        UserResponse request = new UserResponse();
        request.email(email);
        request.name(name);
        request.passwordHash(passwordRaw);

        return sendAsync(() -> userApiClient.addNewUser(request), callback, null, null);
    }


    public CompletableFuture<TokenResponse> postLogin(String email, String passwordRaw, Consumer<TokenResponse> callback){

        LogUserInRequest request = new LogUserInRequest();
        request.email(email);
        request.password(passwordRaw);

        return sendAsync(() -> authApiClient.logUserIn(request), callback, null, null);
    }

    public CompletableFuture<UserResponse> getUser(Consumer<UserResponse> callback){

        return sendAsync(() -> userApiClient.getUserDetails(), callback, null, null);
    }

    public CompletableFuture<GetBinsFullness200Response> getBinFullness(Consumer<GetBinsFullness200Response> callback){

        OffsetDateTime updated = session == null ? null : DateHelpers.toOffsetTime(session.lastLogin());
        return sendAsync(() -> binApiClient.getBinsFullness(updated), callback, null, null);
    }

    public void getCommunity(Consumer<TrashDBLeaderboardResponse> callback){

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

    public void setSession(Session session) {

        this.session = session;

        client.setRequestInterceptor(builder -> builder.header("Authorization", "Bearer " + (session != null ? session.token() : "")));
        initApis();
    }

    private void initApis() {
        defaultApiClient = new DefaultApi(client);
        userApiClient = new UserApi(client);
        authApiClient = new AuthApi(client);
        binApiClient = new BinApi(client);
    }
}