package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seng202.team1.types.user.User;

import static seng202.team1.helpers.StringHelpers.toHex;
import static seng202.team1.helpers.StringHelpers.toInitials;

public class UIAvatar extends StackPane {

    private final Circle circle = new Circle();
    private final Label initials = new Label();

    public UIAvatar(User user) {
        this(user, 40);
    }

    public UIAvatar(User user, double size) {

        setStyle(String.format("-fx-max-width: %dpx; -fx-max-height: %dpx;", (int)size, (int)size));
        circle.setRadius(size / 2);

        initials.setStyle(String.format("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: %dpx;", (int)size/3));
        initials.setMaxWidth(Double.MAX_VALUE);
        initials.setAlignment(Pos.CENTER);

        getChildren().addAll(circle, initials);
        setUser(user);
    }

    public void setUser(User user) {
        circle.setStyle("-fx-fill: " + toHex(userToColour(user)) + ";");
        initials.setText(toInitials(user));
    }

    private static Color userToColour(User user) {

        if(user == null)
            return Color.WHITE;

        double colur = user.name().hashCode() % 255;
        return Color.hsb(colur, 0.50, 0.75);
    }
}