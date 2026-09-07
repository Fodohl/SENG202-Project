package seng202.team1.api.photon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.api.ApiClient;

import java.util.Map;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;

public class PhotonClient extends ApiClient {

    /// PhotonClient specific logger
    static final Logger log = LogManager.getLogger(PhotonClient.class);

    private final Integer LIMIT_LOCATIONS = 3;

    private final Double SEARCH_PRIO_LAT = -43.5233;
    private final Double SEARCH_PRIO_LON = 172.5822;

    public PhotonClient(){
        super("https://photon.komoot.io/api/");
    }

    public void searchLocation(String address, Consumer<PhotonLocationResponse> callback){

        Map<String, String> params = Map.of("limit", LIMIT_LOCATIONS.toString(),
                                            "q", address,
                                            "lat", SEARCH_PRIO_LAT.toString(),
                                            "lon", SEARCH_PRIO_LON.toString()
                                            );

        GET("", params, json -> {
            try {
                System.out.println(json);
                callback.accept(GSON.fromJson(json, PhotonLocationResponse.class));

            } catch (Exception e) {
                log.warn(String.format("Failed to parse PhotonLocationResponse: %s", e.getMessage()));
            }
        });

    }
}
