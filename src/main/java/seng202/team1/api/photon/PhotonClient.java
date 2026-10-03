package seng202.team1.api.photon;

import seng202.team1.api.ApiBackend;
import seng202.team1.gui.components.BlockableUISet;
import seng202.team1.gui.components.UIKeyValuePair;
import seng202.team1.gui.components.UISpinner;
import seng202.team1.types.photon.Feature;
import seng202.team1.types.photon.PhotonLocationResponse;
import seng202.team1.types.photon.Properties;

import java.util.Map;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;
import static seng202.team1.gui.GraphicsCommon.NO_LOADING_UI;

/**
 * API Handler for calls to the Photon geocoding service
 */
public class PhotonClient extends ApiBackend {

    /// Maximum number of locations to fetch
    private final Integer LIMIT_LOCATIONS = 3;

    /// Latitude position to bias searches around
    private final Double SEARCH_PRIO_LAT = -43.5233;

    /// Longitude position to bias searches around
    private final Double SEARCH_PRIO_LON = 172.5822;

    /**
     * Construct an new API client for Photon
     */
    public PhotonClient(){
        super("https://photon.komoot.io/api/");
    }

    /**
     * Search for a location based on the address
     *
     * @see #searchLocation(String, Consumer, UIKeyValuePair, UISpinner)
     */
    public void searchLocation(String address, Consumer<PhotonLocationResponse> callback){
        searchLocation(address, callback, NO_LOADING_UI);
    }

    /**
     * Search for a location based on the given address
     *
     * @param address The address to search
     * @param callback ASYNC callback to receive the response
     * @param error UI Element to show errors on
     * @param loader UI Element to show whilst waiting for response
     */
    public void searchLocation(String address, Consumer<PhotonLocationResponse> callback, BlockableUISet frontend){

        Map<String, String> params = Map.of("limit", LIMIT_LOCATIONS.toString(),
                                            "q", address,
                                            "lat", SEARCH_PRIO_LAT.toString(),
                                            "lon", SEARCH_PRIO_LON.toString()
                                            );

        httpGET("", params, json -> {
            try {
                callback.accept(GSON.fromJson(json, PhotonLocationResponse.class));

            } catch (Exception e) {
                log.warn(String.format("Failed to parse PhotonLocationResponse: %s", e.getMessage()));
            }

        }, frontend);
    }


    /**
     * Convert a photon location feature to a readable address
     *
     * @param feature The location
     * @return The assembled address
     */
    public static String featureToAddress(Feature feature){

        Properties properties = feature.properties();

        boolean isAddress = properties.name() == null;

        return isAddress ? String.format("%s %s, %s, %s", properties.housenumber(), properties.street(), properties.city(), properties.postcode())
                         : String.format("%s, %s, %s", properties.name(), properties.city(), properties.county());
    }
}
