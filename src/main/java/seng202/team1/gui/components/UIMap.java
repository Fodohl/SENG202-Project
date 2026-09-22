package seng202.team1.gui.components;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.common.URLHelpers;
import seng202.team1.data.route.RouteInstruction;
import seng202.team1.state.GlobalState;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static seng202.team1.TrashApp.GSON;

/**
 * A UI Component to handle rendering maps
 * <p>
 * Loads a leaflet HTML map into a container and provides a API functions to interact with the map inside the webview.
 * Map JS errors will be logged to stdio
 * <p>
 * Warning: API calls are NOT synchronous, calls may be queued to run later when web engine is ready to run them
<<<<<<< HEAD
 * <p>
=======
 *
>>>>>>> main
 * JSObject work around as depricated builds on the idea of (2) to inject an /api/ route that is passed to here as the
 * backend (3)
 *
 * @credit (1) https://stackoverflow.com/questions/35985601/calling-a-java-method-from-a-javafx-webview - re web callbacks
 * @credit (2) https://stackoverflow.com/questions/48855972/webview-javafx-redirect-a-certain-url-when-accessed
 * @credit (3) https://stackoverflow.com/questions/31160253/how-to-handle-html-form-in-javafx-application
 * @credit (4) https://stackoverflow.com/questions/5554217/deserialize-a-listt-object-with-gson
<<<<<<< HEAD
 * @todo performance improvements: local tile cache backend
=======
>>>>>>> main
 */
public class UIMap extends UIWebRenderer {

    /// UIMap specific logger
    private static final Logger log = LogManager.getLogger(UIMap.class);

    /// List of callbacks to execute when a marker is clicked
    private List<Consumer<Integer>> pointClickCallbacks = new ArrayList<>();

    /// List of callbacks to execute when a marker is clicked
    private List<BiConsumer<Double, Double>> posClickCallbacks = new ArrayList<>();

    private List<Consumer<List<RouteInstruction>>> mapRoutedCallbacks = new ArrayList<>();

    Type listType = new TypeToken<ArrayList<RouteInstruction>>() {
    }.getType();

    /**
     * Constructs a new UIMap
     */
    public UIMap() {
        super("map");

        // Add styles
        getStyleClass().add("map-container");
        getStyleClass().add("dark-tiles");

    }

<<<<<<< HEAD
=======
    /**
     * Handle a webview function call
     *
     * @param function The function that the JS code called
     * @param params Any arguments for the function
     */
>>>>>>> main
    @Override
    public void handleFunctionCall(String function, Map<String, String> params) {
        switch (function) {

            case "onPointClicked": {
                onPointClicked(params);
                break;
            }

            case "onPosClicked": {
                onPosClicked(params);
                break;
            }

            case "onMapRouted": {
                onMapRouted(params);
                break;
            }

            default: {
                throw new IllegalArgumentException("UIMap unknown api call");
            }

        }
    }


    /**
     * Add a marker to the map
     *
     * @param lat The latitudinal position of the marker
     * @param lng The longitudinal position of the marker
     */
    public void addPoint(double lat, double lng, int markerID) {
        addPoint(lat, lng, markerID, "default");
    }

    /**
     * Add a marker to the map
     *
     * @param lat The latitudinal position of the marker
     * @param lng The longitudinal position of the marker
     */
    public void addPoint(double lat, double lng, int markerID, String path) {
        String js = String.format("marker = L.marker([%f, %f], { icon: makeIcon(L, '%s') })" +
                        ".on('click', e => { " +
                        "onPointClicked(%d)" +
                        "})" +
                        ".on('dblclick', function(e) {" +
                        "L.DomEvent.stopPropagation(e);" +
                        "});" +
                        "markers.addLayer(marker);" +
                        "markerMap[%d] = marker;"
                , lat, lng, path, markerID, markerID);
        call(js);
    }

<<<<<<< HEAD
=======
    /**
     * Remove a point from the map if it exists
     *
     * @param markerID The ID of the point on the map
     */
>>>>>>> main
    public void removePoint(int markerID) {
        String js = String.format(
                "if (markerMap[%d]) { " +
                        "   markers.removeLayer(markerMap[%d]); " +
                        "   map.removeLayer(markerMap[%d]); " +
                        "   delete markerMap[%d]; " +
                        "}", markerID, markerID, markerID, markerID);
        call(js);
    }

<<<<<<< HEAD
=======
    /**
     * Set a route to be rendered between two points on the map
     *
     * @param startLat The start latitude position
     * @param startLng The start longitude position
     * @param endLat The end latitude position
     * @param endLng The end longitude position
     */
>>>>>>> main
    public void setRoute(double startLat, double startLng, double endLat, double endLng) {
        String js = String.format("setRoute(%f,%f,%f,%f);", startLat, startLng, endLat, endLng);
        call(js);
    }

<<<<<<< HEAD
=======
    /**
     * Clear any shown route
     */
>>>>>>> main
    public void clearRoute() {
        String js = String.format("clearRoute();");
        call(js);
    }

    /**
     * Register an event handler for a point click event
<<<<<<< HEAD
     * <p>
=======
     *
>>>>>>> main
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a point on the map is clicked
     * @return True if the callback was registered successfully
     */
    public boolean registerPointClickCallback(Consumer<Integer> callback) {

        // Prevent a callback being triggered twice
        if (pointClickCallbacks.contains(callback))
            return false;

        // Store
        pointClickCallbacks.add(callback);
        return false;
    }

    /**
     * Register an event handler for a position click event
<<<<<<< HEAD
     * <p>
=======
     *
>>>>>>> main
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a position on the map is clicked
     * @return True if the callback was registered successfully
     */
    public boolean registerPossClickCallback(BiConsumer<Double, Double> callback) {

        // Prevent a callback being triggered twice
        if (posClickCallbacks.contains(callback))
            return false;

        // Store
        posClickCallbacks.add(callback);
        return false;
    }

<<<<<<< HEAD
=======
     /**
     * Register an event handler for a map routing event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a route is found between two points
     * @return True if the callback was registered successfully
     */
>>>>>>> main
    public boolean registerMapRouted(Consumer<List<RouteInstruction>> callback) {

        // Prevent a callback being triggered twice
        if (mapRoutedCallbacks.contains(callback))
            return false;

        // Store
        mapRoutedCallbacks.add(callback);
        return false;
    }

<<<<<<< HEAD
=======
    /**
     * Pass the event of a marker on the map being clicked to registered callback
     *
     * @param params Expects params: id={num}
     */
>>>>>>> main
    private void onPointClicked(Map<String, String> params) {

        String id = params.get("id");
        Integer pointId = Objects.equals(id, "null") ? null : Integer.parseInt(id);

        for (Consumer<Integer> callback : pointClickCallbacks)
            callback.accept(pointId);
    }

<<<<<<< HEAD
=======
    /**
     * Pass the event of a point on the map being clicked to registered callback
     *
     * @param params Expects params: lat={float} lng={float}
     */
>>>>>>> main
    private void onPosClicked(Map<String, String> params) {

        String lat = params.get("lat");
        String lng = params.get("lng");

        Double latPos = Objects.equals(lat, "null") ? null : Double.parseDouble(lat);
        Double lngPos = Objects.equals(lng, "null") ? null : Double.parseDouble(lng);

        for (BiConsumer<Double, Double> callback : posClickCallbacks)
            callback.accept(latPos, lngPos);
    }

<<<<<<< HEAD
=======
     /**
     * Pass the event of a route being found to registered callback
     *
     * @param params Expects params: rotue=[{RouteInstruction}]
     */
>>>>>>> main
    private void onMapRouted(Map<String, String> params) {

        List<RouteInstruction> result = GSON.fromJson(params.get("route"), listType);

        for (Consumer<List<RouteInstruction>> callback : mapRoutedCallbacks)
            callback.accept(result);
    }

<<<<<<< HEAD
    public void centreOnPoint(int userPosMarkerID) {
        call(String.format("centerOnMarker(%d);", userPosMarkerID));
=======
    /**
     * Centre the map around a point
     *
     * @param markerId The ID of the marker to centre on
     */
    public void centreOnPoint(int markerId) {
        call(String.format("centerOnMarker(%d);", markerId));
>>>>>>> main
    }
}
