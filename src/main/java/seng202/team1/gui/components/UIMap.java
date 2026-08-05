package seng202.team1.gui.components;

import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class UIMap extends StackPane {

    private static final Logger log = LogManager.getLogger(UIMap.class);

    private WebEngine engine;
    private Queue<String> engineCalls = new ArrayDeque<>();

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
            throw new RuntimeException(e);
        }

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

    private void proccessCalls(){

        //@todo thread to prevent lag

        for(String js : engineCalls)
            call(js);

        engineCalls.clear();
    }

    private void call(String js){

        // Map not ready
        if(engine.getLoadWorker().getState() != Worker.State.SUCCEEDED){
            engineCalls.add(js);
            return;
        }

        engine.executeScript(js);
    }

    public void addPoint(double lat, double lng, String popupText) {
        String js = String.format("L.marker([%f, %f]).addTo(map);", lat, lng, popupText);
        call(js);
    }
}
