package seng202.team1.api;

import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.types.trashdb.ResponseError;
import seng202.team1.api.trashdb.ResponseStatusException;
import seng202.team1.gui.components.BlockableUISet;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static seng202.team1.TrashApp.GSON;
import static seng202.team1.gui.GraphicsCommon.NO_LOADING_UI;

/**
 * A generic client for querying APIs
 *
 * @credit referenced exampled code https://openjdk.org/groups/net/httpclient/recipes.html
 */
public class ApiBackend {

    /// ApiClient specific logger
    protected static final Logger log = LogManager.getLogger(ApiBackend.class);

    /// The url that the API is served on
    protected final String hostUrl;

    /// The HTTP worker to processes requests
    private final HttpClient backend;

    private String bearerToken;

    /**
     * Constructs a new API client
     *
     * @see #ApiBackend(String, String)
     */
    public ApiBackend(String hostUrl) {
        this(hostUrl, null);
    }

    /**
     * Constructs a new API client
     *
     * @param hostUrl The url that the API is served on
     * @param auth    Authentication bearer token if API is private
     */
    public ApiBackend(String hostUrl, String auth) {

        this.hostUrl = hostUrl;
        this.backend = HttpClient.newHttpClient();

    }

    /**
     * Extract the API error message from an exception
     *
     * @param error The error exception
     * @return The extracted message or the error code if cant extract message, if not a API exception will just get the cause
     */
    private static String parseMessage(Throwable error) {

        // Wrapped in a runtime excpetion so pop the stack
        error = error.getCause();

        // Not an api error
        if (!(error.getCause() instanceof ResponseStatusException))
            return error.getMessage();

        ResponseStatusException apiError = (ResponseStatusException) error.getCause();

        try {
            ResponseError message = GSON.fromJson(apiError.getResponseBody(), ResponseError.class);
            return message.error();

        } catch (JsonSyntaxException ignored) {

            // Wansn't an expected error
            return "Request failed (" + apiError.getCode() + ")";
        }
    }

    /**
     * Handle failed API requests and write the error to UI if provided
     *
     * @param message The human-readable error message
     */
    private void requestFailed(BlockableUISet frontend, String message) {

        String errorMessage = String.format("API call failed: %s", message);
        log.warn(errorMessage);

        Platform.runLater(() -> frontend.showError(message));
    }

    /**
     * Run an synchronous API request asynchronously
     *
     * @param request  The request to run that would usually block
     * @param handler  The handler to run when the request completes
     * @param frontend The UI to manipulate whilst waiting for the request to complete
     * @param <T>      The response type
     * @return The thread running the request
     */
    protected <T> CompletableFuture<T> sendAsync(Callable<T> request, Consumer<T> handler, BlockableUISet frontend) {

        // Begin request
        Platform.runLater(() -> frontend.startLoading());
        return CompletableFuture.supplyAsync(() -> {

            // Allow calling the API and handling its errors here rather than in client
            try {
                return request.call();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }


        }).thenApply(response -> {

            // No longer loading
            Platform.runLater(() -> frontend.stopLoading());

            // Run frontend handler on UI thread
            Platform.runLater(() -> {
                handler.accept(response);
            });

            return response;

        }).exceptionally(e -> {
            requestFailed(frontend, parseMessage(e));
            return null;
        });
    }

    /**
     * Send an API request to the endpoint
     *
     * @param request  The request to send
     * @param handler  The async callback to consume the response when received
     * @param frontend The UI group to show during and after requests
     */
    private void send(HttpRequest request, Consumer<String> handler, BlockableUISet frontend) {

        sendAsync(() -> {
            try {
                HttpResponse<String> response = backend.send(request, HttpResponse.BodyHandlers.ofString());

                // Failed
                if (response.statusCode() >= 300)
                    throw new RuntimeException(String.format("%s - %s", response.statusCode(), response.body()));

                return response.body();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }, handler, frontend);
    }

    /**
     * Perform a HTTP GET request on the endpoint
     *
     * @see #httpGET(String, Map, Consumer, BlockableUISet)
     */
    protected void httpGET(String path, Map<String, String> queryParams, Consumer<String> handler) {
        httpGET(path, queryParams, handler, NO_LOADING_UI);
    }

    /**
     * Perform a HTTP GET request on the endpoint
     *
     * @param path        The subpath of the api to query
     * @param queryParams Parameters to pass to the endpoint
     * @param handler     The async callback to consume the response when received
     * @param frontend    The UI to manipulate whilst waiting for the request to complete
     */
    protected void httpGET(String path, Map<String, String> queryParams, Consumer<String> handler, BlockableUISet frontend) {


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
        send(request, handler, frontend);
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
