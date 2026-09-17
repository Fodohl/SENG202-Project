package seng202.team1.gui.components;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import seng202.team1.state.GlobalState;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * A UI Component that handles rendering and interacting with webviews
 */
public abstract class UIWebRenderer extends StackPane {

    /// UIWebRenderer specific logger
    private static final Logger log = LogManager.getLogger(UIWebRenderer.class);

    /// Backend that runs the webview;
    private WebEngine engine;

    protected WebView webView;

    /// List of calls that are waiting to be executed once the JS runtime is ready
    private Queue<String> engineCalls = new ArrayDeque<>();

    /// Whether the webview has finished initialisation
    private boolean webReady = false;

    /// Bridge to pass calls from JavaScript to Java
    public class JavaBridge {
        public void callJavaAPI(String functionName, String jsonParams) {
            Platform.runLater(() -> {
                processAPICall(functionName, jsonParams);
            });
        }
    }

    /// Instance of bridge to do the JS handling
    private final JavaBridge javaBridge = new JavaBridge();

    /**
     * Constructs a new UIWebRenderer
     *
     * @param htmlFile The source code for the webview contents
     */
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

            // Inject API caller for JS side communication
            ((JSObject) engine.executeScript("window")).setMember("javaBridge",javaBridge);
            call(parseAPISourceCode());

            webReady = true;
            System.out.println("ready");
            processCalls();
        });

        // Pipe js logs to south
        webView.getEngine().setOnError(e -> log.error("{}", e));
    }

    /**
     * Process an API call from the JS in the webview
     *
     * @param callURL The full API url that the JS queried
     */
    private void processAPICall(String function, String jsonParams) {

        Map<String, String> params = new HashMap<>();

        // No params
        if(jsonParams == null || jsonParams.isEmpty()){
            processAPICall(function, params);
            return;
        }

        try {
            // Parse JSON into simple string map
            JSONObject json = (JSONObject) new JSONParser().parse(jsonParams);

            for (Object key : json.keySet()) {
                Object val = json.get(key);
                params.put(String.valueOf(key), val != null ? String.valueOf(val) : null);
            }

        } catch (Exception e) {
            log.warn(String.format("Trash App API Call Dropped: %s(%s) - 1 - %s", function, jsonParams, e.getMessage()));
        }


        processAPICall(function, params);
    }

    /**
     * Process an API call from the JS in the webview
     *
     * @param callURL The full API url that the JS queried
     */
    private void processAPICall(String function, Map<String, String> params){

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

            // Drop any packets that do not have required keys or required type
            log.warn(String.format("Trash App API Call Dropped: %s(%s) - 2 - %s", function, params, e.getMessage()));
            return;
        }

        log.info(String.format("Trash App API Call: %s - %s", function, params.toString()));

    }

    public abstract void handleFunctionCall(String function, Map<String, String> params);


    /**
     * Clears the waiting queue of calls to the web engine by running them all
     */
    private void processCalls() {

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

    /**
     * Read the JavaScript file the contains the API helpers for each webview
     *
     * @return The parsed code, will panic if fails otherwise
     */
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
