package seng202.team1.api.photon;

import javafx.scene.shape.ClosePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.api.ApiClient;
import seng202.team1.gui.components.UIKeyValuePair;
import seng202.team1.gui.components.UISpinner;

import java.util.Map;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;

<<<<<<< HEAD
public class PhotonClient extends ApiClient {

    private final Integer LIMIT_LOCATIONS = 3;

    private final Double SEARCH_PRIO_LAT = -43.5233;
    private final Double SEARCH_PRIO_LON = 172.5822;

=======
/**
 * API Handler for calls to the Photon geocoding service
 */
public class PhotonClient extends ApiClient {

    /// Maximum number of locations to fetch
    private final Integer LIMIT_LOCATIONS = 3;

    /// Latitude position to bias searches around
    private final Double SEARCH_PRIO_LAT = -43.5233;

    /// Longitude position to bias searches around
    private final Double SEARCH_PRIO_LON = 172.5822;

    /**
     * Construct an new API client for Photon
     */
>>>>>>> main
    public PhotonClient(){
        super("https://photon.komoot.io/api/");
    }

<<<<<<< HEAD
=======
    /**
     * Search for a location based on the address
     *
     * @see #searchLocation(String, Consumer, UIKeyValuePair, UISpinner)
     */
>>>>>>> main
    public void searchLocation(String address, Consumer<PhotonLocationResponse> callback){
        searchLocation(address, callback, null, null);
    }

<<<<<<< HEAD
=======
    /**
     * Search for a location based on the given address
     *
     * @param address The address to search
     * @param callback ASYNC callback to receive the response
     * @param error UI Element to show errors on
     * @param loader UI Element to show whilst waiting for response
     */
>>>>>>> main
    public void searchLocation(String address, Consumer<PhotonLocationResponse> callback, UIKeyValuePair error, UISpinner loader){

        Map<String, String> params = Map.of("limit", LIMIT_LOCATIONS.toString(),
                                            "q", address,
                                            "lat", SEARCH_PRIO_LAT.toString(),
                                            "lon", SEARCH_PRIO_LON.toString()
                                            );

<<<<<<< HEAD
        GET("", params, json -> {
=======
        httpGET("", params, json -> {
>>>>>>> main
            try {
                callback.accept(GSON.fromJson(json, PhotonLocationResponse.class));

            } catch (Exception e) {
                log.warn(String.format("Failed to parse PhotonLocationResponse: %s", e.getMessage()));
            }

        }, error, loader);
    }
}
