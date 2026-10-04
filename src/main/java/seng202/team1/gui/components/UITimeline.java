package seng202.team1.gui.components;

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

        // Timeline should be guaranteed in chronological order
        List<UITimelineEntry> entries = nodes.stream()
                                             .sorted(Comparator.comparing(UITimelineEntry::getTime))
                                             .toList().reversed();

        getChildren().addAll(entries);
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