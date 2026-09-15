package seng202.team1.gui.components;

import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;

public class UITimeline extends VBox {

    public UITimeline(List<UITimelineEntry> nodes) {
        super(0);

        // Timeline should be guaranteed in chronological order
        List<UITimelineEntry> entries = nodes.stream().sorted(Comparator.comparing(UITimelineEntry::getTime)).toList();
        getChildren().addAll(entries);

    }

}