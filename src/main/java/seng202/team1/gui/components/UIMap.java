package seng202.team1.gui.components;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class UIMap extends StackPane {

    private static final Logger log = LogManager.getLogger(UIMap.class);

    public UIMap(){

        // Add styles
        getStyleClass().add("map-container");
        getStyleClass().add("dark-tiles");

        // Check that the map code exits
        InputStream is = UIMap.class.getResourceAsStream("/pages/map.html");
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
        map.getEngine().loadContent(content);
        getChildren().add(map);

        // Pipe js logs to sout
        map.getEngine().setOnError(e -> log.error("{}", e));
    }
}
