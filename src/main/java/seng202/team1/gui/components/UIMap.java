package seng202.team1.gui.components;

import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.pages.PageType;
import seng202.team1.state.GlobalState;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * A UI Component to handle rendering maps
 *
 * Loads a leaflet HTML map into a container and provides a API functions to interact with the map inside the webview.
 * Map JS errors will be logged to stdio
 *
 * Warning: API calls are NOT synchronous, calls may be queued to run later when web engine is ready to run them
 *
 * @credit https://stackoverflow.com/questions/35985601/calling-a-java-method-from-a-javafx-webview - re web callbacks
 * @todo depcreated, find work around
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

    /// Internal object to expose as a JS interface (internal use only, but must be public for lib to access)
    public class MapBridge {
        public void onPointClicked(int pointID) {
            for(Consumer<Integer> callback : pointClickCallbacks)
                callback.accept(pointID);
        }
    }

    /// Handler for map JS function calls
    private final MapBridge mapBridge = new MapBridge();

    /**
     * Constructs a new UIMap
     */
    public UIMap(){

        // Add styles
        getStyleClass().add("map-container");
        getStyleClass().add("dark-tiles");

        // Check that the map code exits
        InputStream is = UIMap.class.getResourceAsStream("/html/map.html");
        if(is == null)
            throw new NullPointerException("Map code cant be loaded");

        // Read the map code
        String content;
        try{
            content = new String(is.readAllBytes());

        } catch (IOException e) {

            // Map file must exist to work, panic otherwise
            throw new RuntimeException(e);
        }

        // Webview unavailable in headless mode
        if(GlobalState.IS_HEADLESS_MODE)
            return;

        // Render the map as mini website
        WebView map = new WebView();
        engine = map.getEngine();

        // Add the map
        map.getEngine().loadContent(content);
        getChildren().add(map);

        // When map is ready process any waiting calls
        engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {

            // Engine still being setup
            if (newValue != Worker.State.SUCCEEDED)
                return;

            // Inject handler
            JSObject win = (JSObject) engine.executeScript("window");
            win.setMember("app", mapBridge);

            proccessCalls();
        });

        // Pipe js logs to sout
        map.getEngine().setOnError(e -> log.error("{}", e));
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
        if(GlobalState.IS_HEADLESS_MODE || engine.getLoadWorker().getState() != Worker.State.SUCCEEDED){
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
        String js = String.format("L.marker([%f, %f])" +
                    ".addTo(map)" +
                    ".on('click', e => { " +
                        "window.app.onPointClicked(%d)" +
                    "})" +
                    ".on('dblclick', function(e) {" +
                        "L.DomEvent.stopPropagation(e);" +
                    "});"
                , lat, lng, markerID);
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
}
