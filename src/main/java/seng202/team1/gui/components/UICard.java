package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class UICard extends VBox {

    private String iconPath;
    private HBox body;

    private Label valueNode;
    private Label subtitleNode;

    public UICard(String title, String value, String subtitle, String icon) {
        this(title, value, subtitle, icon, false);
    }

    public UICard(String title, String value, String subtitle, String icon, boolean primary){

        this.iconPath = icon;

        int iconSize = 64;
        int spacing = 200;

        if(primary){
            getStyleClass().add("card-main");
            iconSize = 125;
            spacing = 100;
        }

        // Icon value pair
        valueNode = new Label(value);
        valueNode.getStyleClass().add("maintext");
        UIImage iconNode = new UIImage(icon, iconSize, iconSize);
valueNode.widthProperty().addListener((obs, oldW, newW) ->
    valueNode.setMinHeight(valueNode.prefHeight(newW.doubleValue()))
);
        subtitleNode = new Label(subtitle);
        subtitleNode.getStyleClass().add("subtext");
        subtitleNode.setAlignment(Pos.CENTER);

        VBox statText = new VBox(valueNode, subtitleNode);
        statText.setAlignment(Pos.CENTER);

        body = new HBox(spacing, statText, iconNode);
        body.getStyleClass().add("body");

        Label titleNode = new Label(title);
        titleNode.getStyleClass().add("card-title");

        // Build
        getChildren().addAll(titleNode, body);
        getStyleClass().add("card");

    }


    public void resizeIcon(double width, double height){

        // Recreate the icon
        StackPane icon = new UIImage(iconPath, width, height);

        body.getChildren().removeLast();
        body.getChildren().add(icon);
    }

    public void setText(String s) {
        valueNode.setText(s);
    }

    public void setSubtitle(String s) {
        subtitleNode.setText(s);
    }
}
