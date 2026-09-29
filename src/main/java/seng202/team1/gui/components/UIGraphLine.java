package seng202.team1.gui.components;

import javafx.geometry.Insets;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team1.state.GlobalState;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static seng202.team1.state.GlobalState.IS_HEADLESS_MODE;

/**
 * A UI component to handle rendering graphs (line)
 */
public class UIGraphLine extends UIWebRenderer {

    /**
     * Constructs a new line graph
     */
    public UIGraphLine() {
        super("linegraph");
        getStyleClass().add("card");

        // Must have dimensions otherwise will not appear
        setMinHeight(420);
        setMaxHeight(420);

        // Webviews dont run in CI without graphics
        if(IS_HEADLESS_MODE)
            return;

        webView.addEventFilter(ScrollEvent.SCROLL, event -> {

            // Prevent scrolling in the graph
            event.consume();

            // Pass the scroll to the main window
            if (getParent() != null)
                getParent().fireEvent(event);

        });

        // Define the data
        call("data = [{\"name\":\"Jan\",\"value\":280.0},{\"name\":\"Feb\",\"value\":190.0},{\"name\":\"Mar\",\"value\":280.0},{\"name\":\"Apr\",\"value\":300.0},{\"name\":\"May\",\"value\":205.0},{\"name\":\"Jun\",\"value\":430.0}]");
    }

    /**
     * Handle webview function calls
     *
     * @param function The function that was called
     * @param params The parameters of the function call
     */
    @Override
    public void handleFunctionCall(String function, Map<String, String> params) {
        throw new IllegalArgumentException("Graph should not make any API calls");
    }
}