package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;

/**
 * A UI Component for showing a timeline of data
 */
public class UITimeline extends VBox {


    private List<UITimelineEntry> entries;

    /**
     * Constructs a new UITimline sorted by date descending
     *
     * @param nodes The entries in this timeline
     */
    public UITimeline(List<UITimelineEntry> nodes) {
        super(0);

        if(nodes.isEmpty()){
            renderErrorMessage();
            return;
        }

        // Add the 'today' node
        getChildren().add(new UITimelineEntry());

        // Timeline should be guaranteed in chronological order
        List<UITimelineEntry> entries = nodes.stream()
                                             .sorted(Comparator.comparing(UITimelineEntry::getTime))
                                             .toList().reversed();

        getChildren().addAll(entries);
    }

    /**
     * Show an error message for when the user has got nothing to show on the timeline
     */
    private void renderErrorMessage() {

        Label error = new Label("Please log trash at a bin on the map page for it to appear here");
        error.getStyleClass().add("timeline-error");
        getChildren().add(error);

    }


    /**
     * Get the entries this timeline has
     *
     * @return The entries (unordered)
     */
    public List<UITimelineEntry> getEntries() {
        return entries;
    }
}