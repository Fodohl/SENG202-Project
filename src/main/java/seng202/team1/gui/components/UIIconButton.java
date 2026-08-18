package seng202.team1.gui.components;

import javafx.scene.layout.HBox;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.GraphicsCommon;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.pages.Pages;

/**
 * A UI Component to handle rendering buttons with icons
 */
public class UIIconButton extends HBox {

    private String prompt;
    private String iconPath;

    public UIIconButton(String iconPath, String prompt) {

        super();

        this.iconPath = iconPath;
        this.prompt = prompt;

        // Apply styles
        getStyleClass().add("iconbutton");

        // Add icon
        StackPane icon = new UIImage(iconPath, 25, 25);
        icon.getStyleClass().add("iconbutton-icon");

        // Add text
        Label label = new Label(prompt);
        label.getStyleClass().add("iconbutton-text");

        // Combine
        getChildren().addAll(icon, label);

    }

    public String getText(){
        return prompt;
    }

    public void resizeIcon(double width, double height){

        // Recreate the icon
        StackPane icon = new UIImage(iconPath, width, height);

        getChildren().removeFirst();
        getChildren().addFirst(icon);

    }

}