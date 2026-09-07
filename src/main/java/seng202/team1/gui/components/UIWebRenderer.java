package seng202.team1.gui.components;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import seng202.team1.common.URLHelpers;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.state.GlobalState;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

public abstract class UIWebRenderer extends StackPane {


    /// UIWebRenderer specific logger
    private static final Logger log = LogManager.getLogger(UIWebRenderer.class);

    /// Backend that runs the webview;
    private WebEngine engine;

    protected WebView webView;

    /// List of calls that are waiting to be executed once the JS runtime is ready
    private Queue<String> engineCalls = new ArrayDeque<>();

    /// Whether the webview has finished initalisation
    private boolean webReady = false;

    public UIWebRenderer(String htmlFile) {


        // Check that the map code exits
        URL mapUrl = UIMap.class.getResource(String.format("/web/%s.html", htmlFile));
        if (mapUrl == null)
            throw new NullPointerException("HTML code cant be loaded, must be placed in resources/html/ ");

        // Webview unavailable in headless mode
        if(GlobalState.IS_HEADLESS_MODE)
            return;

        // Render the content as mini website
        webView = new WebView();
        engine = webView.getEngine();

        // Add the webpane
        webView.getEngine().load(mapUrl.toExternalForm());
        getChildren().add(webView);

        // When map is ready process any waiting calls
        engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {

            // Engine still being setup
            if (newValue != Worker.State.SUCCEEDED)
                return;

            webReady = true;
            proccessCalls();
        });

        // Inject API handler
        engine.locationProperty().addListener((obs, oldValue, newValue) -> {

            // Prevent trying to go to the address
            engine.getLoadWorker().cancel();

            // Handle call after fx tick
            Platform.runLater(() -> processAPICall(newValue));
        });

        // Inject API caller
        call(parseAPISourceCode());

        // Pipe js logs to sout
        webView.getEngine().setOnError(e -> log.error("{}", e));
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

        try {

            switch (function) {

                case "onConsoleLog": {
                    log.info(params.get("message"));
                    break;
                }

                case "onConsoleWarn" : {
                    log.warn(params.get("message"));
                    break;
                }

                 case "onConsoleError" : {
                    log.error(params.get("message"));
                    break;
                }

                default : {

                    // Delegate to subclass
                    handleFunctionCall(function, params);
                }
            }

        } catch (Exception e){

            // Drop any packets that dont have requried keys or required type
            log.warn(String.format("Trash App API Call Dropped: %s - 2 - %s", callURL, e.getMessage()));
            return;
        }

        log.info(String.format("Trash App API Call: %s - %s", function, params.toString()));

    }

    public abstract void handleFunctionCall(String function, Map<String, String> params);


    /**
     * Clears the waiting queue of calls to the web engine by running them all
     */
    private void proccessCalls() {

        // Run all calls that are waiting to be run
        for (String js : engineCalls)
            call(js);

        engineCalls.clear();
    }

    /**
     * Call a piece of JavaScript code on the webview
     * <p>
     * Warning: not synchronous, calls may be queued to run later when web engine is ready to run them
     *
     * @param js The JavaScript to run in the webview
     */
    protected void call(String js) {

        // Map not ready
        if (GlobalState.IS_HEADLESS_MODE || !webReady) {
            engineCalls.add(js);
            return;
        }

        engine.executeScript(js);
    }

    private String parseAPISourceCode(){
        InputStream is = UIWebRenderer.class.getClassLoader().getResourceAsStream("web/js/api.js");

        // File must exist for API calls to be sent via HTML
        if(is == null)
            throw new NullPointerException("API Source Code does not exist");

        try {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("API Source Code invalid");
        }
    }
}
