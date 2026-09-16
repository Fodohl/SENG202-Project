package seng202.team1.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import seng202.team1.common.StringHelpers;

import java.time.Instant;

public class UITimelineEntry extends HBox {

    private final int TIMELINE_WIDTH = 5;

    private Instant timestamp;

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
        this.getChildren().addAll(detailsContainer, dateContainer, connector, verticalAxis);
    }

    public Instant getTime(){
        return timestamp;
    }
}