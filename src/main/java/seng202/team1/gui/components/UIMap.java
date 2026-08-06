package seng202.team1.gui.components;

import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.state.GlobalState;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A UI Component to handle rendering maps
 *
 * Loads a leaflet HTML map into a container and provides a API functions to interact with the map inside the webview.
 * Map JS errors will be logged to stdio
 *
 * Warning: API calls are NOT synchronous, calls may be queued to run later when web engine is ready to run them
 *
 * @todo thread js call queue
 * @todo GPU acx
 */
public class UIMap extends StackPane {

    /// UIMap specific logger
    private static final Logger log = LogManager.getLogger(UIMap.class);

    /// Backend that runs the map
    private WebEngine engine;

    /// List of calls that are waiting to be executed once the JS runtime is ready
    private Queue<String> engineCalls = new ArrayDeque<>();

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
     *
     * @todo allow for onClick callbacks
     */
    public void addPoint(double lat, double lng) {
        String js = String.format("L.marker([%f, %f]).addTo(map);", lat, lng);
        call(js);
    }
}
