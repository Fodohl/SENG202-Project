package seng202.team1.gui.components;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team1.state.GlobalState;
import seng202.team1.types.gui.GraphPoint;
import seng202.team1.types.trashdb.CommunityCleanliness;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static seng202.team1.TrashApp.GSON;
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
        if (IS_HEADLESS_MODE)
            return;

        webView.addEventFilter(ScrollEvent.SCROLL, event -> {

            // Prevent scrolling in the graph
            event.consume();

            // Pass the scroll to the main window
            if (getParent() != null)
                getParent().fireEvent(event);

        });

    }

    /**
     * Sets the data for the graph and updates the WebView chart
     *
     * @param cleanlinessList List of cleanliness records
     */
    public void setData(List<CommunityCleanliness> cleanlinessList) {
        if (IS_HEADLESS_MODE || cleanlinessList == null) {
            return;
        }

        // Convert into a stringified JSON
        List<GraphPoint> points = cleanlinessList.stream()
                .map(c -> GraphPoint.fromCCleanliness(c))
                .toList();

        String jsonArray = GSON.toJson(points);

        call(String.format("setGraphData(%s);", jsonArray));
    }

    /**
     * Constructs a new blank object as the java bridge, not needed for the line graph
     *
     * @return empty object
     */
    @Override
    protected Object newJavaBridge() {
        return new Object();
    }
}