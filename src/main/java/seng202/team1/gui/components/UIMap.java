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
 *
 * Loads a leaflet HTML map into a container and provides a API functions to interact with the map inside the webview.
 * Map JS errors will be logged to stdio
 *
 * Warning: API calls are NOT synchronous, calls may be queued to run later when web engine is ready to run them
 *
 * JSObject work around as depricated builds on the idea of (2) to inject an /api/ route that is passed to here as the
 * backend (3)
 *
 * @credit (1) https://stackoverflow.com/questions/35985601/calling-a-java-method-from-a-javafx-webview - re web callbacks
 * @credit (2) https://stackoverflow.com/questions/48855972/webview-javafx-redirect-a-certain-url-when-accessed
 * @credit (3) https://stackoverflow.com/questions/31160253/how-to-handle-html-form-in-javafx-application
 * @credit (4) https://stackoverflow.com/questions/5554217/deserialize-a-listt-object-with-gson
 *
 * @todo performance improvements: local tile cache backend
 */
public class UIMap extends StackPane {

    /// UIMap specific logger
    private static final Logger log = LogManager.getLogger(UIMap.class);

    /// Backend that runs the map
    private WebEngine engine;

    /// List of calls that are waiting to be executed once the JS runtime is ready
    private Queue<String> engineCalls = new ArrayDeque<>();

    /// List of callbacks to execute when a marker is clicked
    private List<Consumer<Integer>> pointClickCallbacks = new ArrayList<>();

    /// List of callbacks to execute when a marker is clicked
    private List<BiConsumer<Double, Double>> posClickCallbacks = new ArrayList<>();

    private boolean mapReady = false;

    Type listType = new TypeToken<ArrayList<RouteInstruction>>(){}.getType();

    /**
     * Constructs a new UIMap
     */
    public UIMap(){

        // Add styles
        getStyleClass().add("map-container");
        getStyleClass().add("dark-tiles");

        // Check that the map code exits
        URL mapUrl = UIMap.class.getResource("/html/map.html");
        if (mapUrl == null)
            throw new NullPointerException("Map code cant be loaded");

        // Webview unavailable in headless mode
        if(GlobalState.IS_HEADLESS_MODE)
            return;

        // Render the map as mini website
        WebView map = new WebView();
        engine = map.getEngine();

        // Add the map
        map.getEngine().load(mapUrl.toExternalForm());
        getChildren().add(map);

        // When map is ready process any waiting calls
        engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {

            // Engine still being setup
            if (newValue != Worker.State.SUCCEEDED)
                return;

            mapReady = true;
            proccessCalls();
        });

        // Inject handler
        engine.locationProperty().addListener((obs, oldValue, newValue) -> {

            // Prevent trying to go to the address
            engine.getLoadWorker().cancel();

            // Handle call
            Platform.runLater(() -> processAPICall(newValue));
        });

        // Pipe js logs to sout
        map.getEngine().setOnError(e -> log.error("{}", e));
        addPoint(0,0,-50,"default");
    }

    private void processAPICall(String callURL){

        // Parse the call
        URI callData;
        try{
            callData = new URI(callURL);
            callData = new URI(callData.getRawFragment());
        } catch (URISyntaxException e) {
            log.warn(String.format("Trash App API Call Dropped: %s - 1 - %s", callURL, e.getMessage()));
            return;
        }

        // Extract the call info
        String function = URLHelpers.getPage(callData);
        Map<String, String> params = URLHelpers.getParams(callData);

        // Drop any packets that dont have requried keys or required type
        try {
            switch (function){

                case "onPointClicked" : {
                    onPointClicked(params);
                    break;
                }

                case "onPosClicked" : {
                    onPosClicked(params);
                    break;
                }

                case "onConsoleLog" : {
                    System.out.println(params.get("message"));
                    break;
                }

                case "onMapRouted" : {
                    onMapRouted(params);
                    break;
                }

            }

        } catch (Exception e){
            log.warn(String.format("Trash App API Call Dropped: %s - 2 - %s", callURL, e.getMessage()));
            return;
        }

        log.info(String.format("Trash App API Call: %s - %s", function, params.toString()));

    }

    /**
     * Clears the waiting queue of calls to the web engine by running them all
     */
    private void proccessCalls(){

        // Run all calls that are waiting to be ran
        for(String js : engineCalls)
            call(js);

        engineCalls.clear();
    }

    /**
     * Call a piece of JavaScript code on the map's webview
     *
     * Warning: not synchronous, calls may be queued to run later when web engine is ready to run them
     *
     * @param js The JavaScript to run in the webview
     */
    private void call(String js){

        // Map not ready
        if(GlobalState.IS_HEADLESS_MODE || !mapReady){
            engineCalls.add(js);
            return;
        }

        engine.executeScript(js);
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

    public void removePoint(int markerID) {
        String js = String.format(
            "if (markerMap[%d]) { " +
            "   markers.removeLayer(markerMap[%d]); " +
            "   map.removeLayer(markerMap[%d]); " +
            "   delete markerMap[%d]; " +
            "}", markerID, markerID, markerID, markerID);
        call(js);
    }

    public void setRoute(double startLat, double startLng, double endLat, double endLng){
        String js = String.format("setRoute(%f,%f,%f,%f);", startLat, startLng, endLat, endLng);
        call(js);
    }

    public void clearRoute(){
        String js = String.format("clearRoute();");
        call(js);
    }

    /**
     * Register an event handler for a point click event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a point on the map is clicked
     * @return True if the callback was registered successfully
     */
    public boolean registerPointClickCallback(Consumer<Integer> callback){

        // Prevent a callback being triggered twice
        if(pointClickCallbacks.contains(callback))
            return false;

        // Store
        pointClickCallbacks.add(callback);
        return false;
    }

    /**
     * Register an event handler for a position click event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a position on the map is clicked
     * @return True if the callback was registered successfully
     */
    public boolean registerPossClickCallback(BiConsumer<Double, Double> callback){

        // Prevent a callback being triggered twice
        if(posClickCallbacks.contains(callback))
            return false;

        // Store
        posClickCallbacks.add(callback);
        return false;
    }

    private void onPointClicked(Map<String, String> params){

        String id = params.get("id");
        Integer pointId = Objects.equals(id, "null") ? null : Integer.parseInt(id);

        for(Consumer<Integer> callback : pointClickCallbacks)
                callback.accept(pointId);
    }

    private void onPosClicked(Map<String, String> params){

        String lat = params.get("lat");
        String lng = params.get("lng");

        Double latPos = Objects.equals(lat, "null") ? null : Double.parseDouble(lat);
        Double lngPos = Objects.equals(lng, "null") ? null : Double.parseDouble(lng);

        for(BiConsumer<Double, Double> callback : posClickCallbacks)
                callback.accept(latPos, lngPos);
    }

    private void onMapRouted(Map<String, String> params){

        List<RouteInstruction> result = GSON.fromJson(params.get("route"), listType);

        for(RouteInstruction r : result)
            System.out.println(r);

    }
}
