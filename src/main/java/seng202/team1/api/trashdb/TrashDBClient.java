package seng202.team1.api.trashdb;

import seng202.team1.api.ApiClient;
import seng202.team1.api.photon.PhotonLocationResponse;

import java.util.Map;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;

public class TrashDBClient extends ApiClient {

    private final Integer LIMIT_LEADERBOARD_ENTRIES = 3;


    public TrashDBClient(){
        super("https://api.trashapp.com/v1/");
    }


    public void getLeaderboard(Consumer<TrashDBLeaderboardResponse> callback){

        String mockJson = """
                {
                  "entries": [
                    {
                      "position": 1,
                      "user_id": 101,
                      "name": "John S",
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
}
