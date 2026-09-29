package seng202.team1.api;

import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.components.UIIconText;
import seng202.team1.gui.components.UIInputComponent;
import seng202.team1.gui.components.UIKeyValuePair;
import seng202.team1.gui.components.UISpinner;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A generic client for querying APIs
 *
 * @credit referenced exampled code https://openjdk.org/groups/net/httpclient/recipes.html
 */
public class ApiClient {

    /// ApiClient specific logger
    protected static final Logger log = LogManager.getLogger(ApiClient.class);

    /// The url that the API is served on
    private final String hostUrl;

    /// The HTTP worker to processes requests
    private final HttpClient backend;

    /**
     * Constructs a new API client
     *
     * @see #ApiClient(String, String)
     */
    public ApiClient(String hostUrl) {
        this(hostUrl, null);
    }

    /**
     * Constructs a new API client
     *
     * @param hostUrl The url that the API is served on
     * @param auth Authentication bearer token if API is private
     */
    public ApiClient(String hostUrl, String auth) {

        this.hostUrl = hostUrl;
        this.backend = HttpClient.newHttpClient();

    }

    /**
     * Safely update the visibility of a loader element
     *
     * @param loader The loader element
     * @param isVisible Whether the element should be shown or not
     */
    private void trySetLoader(Node loader, boolean isVisible){

        // Some UI may do background requests only
        if(loader != null)
            loader.setVisible(isVisible);
    }

    /**
     * Handle failed API requests and write the error to UI if provided
     *
     * @param request The failing request
     * @param error The error element to show
     * @param loader The loader to stop loading
     * @param message The human-readable error message
     */
    private void requestFailed(HttpRequest request, UIKeyValuePair error, UISpinner loader, String message){

        // No longer loading
        trySetLoader(loader, false);

        String errorMessage = String.format("API call to %s failed: %s", request.uri().toString(), message);
        log.warn(errorMessage);

        error.setVisible(true);
        error.setText(message);
    }

    /**
     * Send an API request to the endpoint
     *
     * @param request The request to send
     * @param handler The async callback to consume the response when received
     * @param error The optional error element to write errors to
     * @param loader The optional element to show whilst loading
     */
    private void send(HttpRequest request, Consumer<String> handler, UIKeyValuePair error, UISpinner loader){

        //@todo super should map error to human readable

        // Begin request
        trySetLoader(loader, true);
        backend.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {

            // No longer loading
            trySetLoader(loader, false);

            // Failed
            if(response.statusCode() >= 300){
                requestFailed(request, error, loader, String.format("%s - %s", response.statusCode(), response.body()));
                return;
            }

            // Run frontend handler on UI thread
            Platform.runLater(() -> {
                handler.accept(response.body());
            });


        }).exceptionally(e -> {
            requestFailed(request, error, loader, e.getMessage());
            return null;
        });

    }

    /**
     * Perform a HTTP GET request on the endpoint
     *
     * @see #httpGET(String, Map, Consumer, UIKeyValuePair, UISpinner)
     */
    protected void httpGET(String path, Map<String, String> queryParams, Consumer<String> handler) {
        httpGET(path, queryParams, handler, null, null);
    }

    /**
     * Perform a HTTP GET request on the endpoint
     *
     * @param path The subpath of the api to query
     * @param queryParams Parameters to pass to the endpoint
     * @param handler The async callback to consume the response when received
     * @param error The optional error element to write errors to
     * @param loader The optional element to show whilst loading
     */
    protected void httpGET(String path, Map<String, String> queryParams, Consumer<String> handler, UIKeyValuePair error, UISpinner loader) {

        // @todo check cache

        // Build the full url
        String queryString = queryParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                                                URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String fullURL = String.format("%s/%s?%s", hostUrl, path, queryString);

        // Build the query
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(fullURL))
            .header("Accept", "application/json")
            .header("User-Agent", "TrashApp/1.0")
            .GET()
            .build();

        // Send req
        send(request, handler, error, loader);
    }
}
