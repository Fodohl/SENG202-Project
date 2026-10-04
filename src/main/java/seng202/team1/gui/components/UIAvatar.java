package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seng202.team1.types.user.User;

import static seng202.team1.helpers.StringHelpers.toHex;
import static seng202.team1.helpers.StringHelpers.toInitials;

/**
 * A UI Component to handle rendering user icons
 */
public class UIAvatar extends StackPane {

    /// The background of the icon
    private final Circle circle = new Circle();

    /// The initials of the user this icon belongs to
    private final Label initials = new Label();

    /**
     * Constructs a new UIAvatar
     *
     * @param user The user the avatar should belong to
     */
    public UIAvatar(User user) {
        this(user, 40);
    }

    /**
     * Constructs a new UIAvatar
     *
     * @param user The user the avatar should belong to
     * @param size The size of the icon
     */
    public UIAvatar(User user, double size) {

        setStyle(String.format("-fx-max-width: %dpx; -fx-max-height: %dpx;", (int) size, (int) size));
        circle.setRadius(size / 2);

        initials.setStyle(String.format("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: %dpx;", (int) size / 3));
        initials.setMaxWidth(Double.MAX_VALUE);
        initials.setAlignment(Pos.CENTER);

        getChildren().addAll(circle, initials);
        setUser(user);
    }

    /***
     * Update the user this icon is for
     *
     * @param user The new user
     */
    public void setUser(User user) {
        circle.setStyle("-fx-fill: " + toHex(userToColour(user)) + ";");
        initials.setText(toInitials(user));
    }

    /**
     * Get a background colour for a given user
     *
     * @param user The user to get the colour for
     * @return The colour for the user
     */
    private static Color userToColour(User user) {

        if (user == null)
            return Color.WHITE;

        double colur = user.name().hashCode() % 255;
        return Color.hsb(colur, 0.50, 0.75);
    }
}