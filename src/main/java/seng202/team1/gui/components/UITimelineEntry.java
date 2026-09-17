package seng202.team1.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import seng202.team1.common.StringHelpers;

import java.time.Instant;

/**
 * A UI Component for showing a point in a timeline
 */
public class UITimelineEntry extends HBox {

    /// When in the timeline this entry should go
    private Instant timestamp;

    /**
     * Constructs a new chunk node for the timeline that represents this entry
     *
     * @param timestamp When in the timeline this entry should go
     * @param icon The icon that categorises the timeline
     * @param title The entry title
     * @param text The entry information
     */
    public UITimelineEntry(Instant timestamp, UIImage icon, String title, String text) {

        super();
        this.timestamp = timestamp;

        setAlignment(Pos.CENTER);
        getStyleClass().add("timeline");

        Label titleLabel = new Label(title);
        Label textLabel = new Label(text);

        textLabel.getStyleClass().add("subtext");
        textLabel.setWrapText(true);

        // Combine title and subtext into a box to the left of the timeline
        VBox detailsContainer = new VBox(5, titleLabel, textLabel);
        detailsContainer.getStyleClass().add("timeline-details");

        Circle dateBorder = new Circle(100);
        dateBorder.getStyleClass().add("timeline-date-bg");
        Label dateText = new Label(StringHelpers.toLocaleString(timestamp));

        // Combine icon and date
        VBox iconDateBox = new VBox(15, icon, dateText);
        iconDateBox.setAlignment(Pos.CENTER);
        StackPane dateContainer = new StackPane(dateBorder, iconDateBox);
        dateContainer.getStyleClass().add("timeline-date");

        // Line to indicate place on timeline
        Line connector = new Line();
        connector.setStartX(0);
        connector.setEndX(100);
        connector.getStyleClass().add("timeline-connector");

        // Ensure the line part of the timeline grows so that it touches start end of neighbouring chunks
        Region timelineChunk = new Region();
        timelineChunk.getStyleClass().add("timeline-line-chunk");
        timelineChunk.prefHeightProperty().bind(this.heightProperty());

        // Indicator of position in timeline
        Circle dot = new Circle(7);
        dot.getStyleClass().add("timeline-indicator");

        // This entries portion of the timeline axis
        StackPane verticalAxis = new StackPane(timelineChunk, dot);
        verticalAxis.setPrefWidth(30);

        // Combine
        this.getChildren().addAll(detailsContainer,  dateContainer, connector, verticalAxis);
    }

    /**
     * Get the point in time this entry represents
     *
     * @return The instant this entry represents
     */
    public Instant getTime(){
        return timestamp;
    }
}