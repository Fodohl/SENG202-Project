package seng202.team1.api;

import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.components.UIIconText;
import seng202.team1.gui.components.UIInputComponent;
import seng202.team1.gui.components.UIKeyValuePair;

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

//@todo take cache once new storage design done
//@credit example code https://openjdk.org/groups/net/httpclient/recipes.html

public class ApiClient {

    /// ApiClient specific logger
    static final Logger log = LogManager.getLogger(ApiClient.class);

    private final String hostUrl;

    private final HttpClient backend;

    public ApiClient(String hostUrl) {
        this(hostUrl, null);
    }

    public ApiClient(String hostUrl, String auth) {

        this.hostUrl = hostUrl;
        this.backend = HttpClient.newHttpClient();

    }

    private void trySetLoader(Node loader, boolean isVisible){

        // Some UI may do background requests only
        if(loader != null)
            loader.setVisible(isVisible);
    }

    private void requestFailed(HttpRequest request, UIKeyValuePair error, Node loader, String message){

        // No longer loading
        trySetLoader(loader, false);

        String errorMessage = String.format("API call to %s failed: %s", request.uri().toString(), message);
        log.warn(errorMessage);

        error.setVisible(true);
        error.setText(message);
    }

    private void send(HttpRequest request, Consumer<String> handler, UIKeyValuePair error, Node loader){

        //@todo show error human readable

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

    protected void GET(String path, Map<String, String> queryParams, Consumer<String> handler) {
        GET(path, queryParams, handler, null, null);
    }

    protected void GET(String path, Map<String, String> queryParams, Consumer<String> handler, UIKeyValuePair error, Node loader) {

        // @todo check cache


        // Build the full url
        String queryString = queryParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                                                URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String fullURL = String.format("%s/%s?%s", hostUrl, path, queryString);
        System.out.println(fullURL);

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
