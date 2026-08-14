package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import javafx.scene.control.Label;

public class UIIconText extends HBox {

   public UIIconText(String iconName, String textValue){
        super(5);

        // Build
        UIImage icon = new UIImage(String.format("/images/%s.svg", iconName), 20, 20);
        Label text = new Label(textValue);
        getChildren().addAll(icon, text);

        // Style
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("icon-text");
   }
}
