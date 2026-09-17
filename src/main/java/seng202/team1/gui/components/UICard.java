package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * A UI Component to handle rendering a card
 */
public class UICard extends VBox {

    /// The icon to show in the card
    private String iconPath;

    /// The text section of the card
    private HBox body;

    /// The statistic being shown in the card
    private Label valueNode;

    /// The statistic unit or similar
    private Label subtitleNode;

    /**
     * Construct a new UICard
     *
     * @see #UICard(String, String, String, String, boolean)
     */
    public UICard(String title, String value, String subtitle, String icon) {
        this(title, value, subtitle, icon, false);
    }

    /**
     * Construct a new UICard
     *
     * @param title The title to show in the UI card
     * @param value The value that the UI card displays
     * @param subtitle The subtext under the value, ie a unit
     * @param icon The icon relating to the stat
     * @param primary Whether it is a primary (full width) or secondard (third width) card
     */
    public UICard(String title, String value, String subtitle, String icon, boolean primary){

        this.iconPath = icon;

        int iconSize = 64;

        if(primary){
            getStyleClass().add("card-main");
            iconSize = 125;
        }

        // Icon value pair
        valueNode = new Label(value);
        valueNode.getStyleClass().add("maintext");
        UIImage iconNode = new UIImage(icon, iconSize, iconSize);

        // Ensure gap grows
        valueNode.widthProperty().addListener((obs, oldW, newW) ->
            valueNode.setMinHeight(valueNode.prefHeight(newW.doubleValue()))
        );

        subtitleNode = new Label(subtitle);
        subtitleNode.getStyleClass().add("subtext");
        subtitleNode.setAlignment(Pos.CENTER);

        VBox statText = new VBox(valueNode, subtitleNode);
        statText.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        body = new HBox(statText, spacer, iconNode);
        body.getStyleClass().add("body");

        Label titleNode = new Label(title);
        titleNode.getStyleClass().add("card-title");

        // Build
        getChildren().addAll(titleNode, body);
        getStyleClass().add("card");

    }

    /**
     * Replace the icon in the middle with one of the same contents but different dims
     *
     * @param width The new width value of the icon
     * @param height The new height value of the icon
     */
    public void resizeIcon(double width, double height){

        // Recreate the icon
        StackPane icon = new UIImage(iconPath, width, height);

        body.getChildren().removeLast();
        body.getChildren().add(icon);
    }

    /**
     * Set the value of the stat in the card
     *
     * @param s The new value text
     */
    public void setText(String s) {
        valueNode.setText(s);
    }

    /**
     * Set the subtitle
     *
     * @param s The new subtitle
     */
    public void setSubtitle(String s) {
        subtitleNode.setText(s);
    }
}
