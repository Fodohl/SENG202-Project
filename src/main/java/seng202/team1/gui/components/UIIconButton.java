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

<<<<<<< HEAD
    private String prompt;
    private String iconPath;

=======
    /// The button prompt
    private String prompt;

    /// The icon prompt
    private String iconPath;

    /**
     * Constructs a new UIIconButton
     *
     * @param iconPath The path to the icon image file
     * @param prompt The prompt to show on the button
     */
>>>>>>> main
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

<<<<<<< HEAD
=======
    /**
     * Get the prompt on the button
     *
     * @return The prompt text
     */
>>>>>>> main
    public String getText(){
        return prompt;
    }

<<<<<<< HEAD
=======
    /**
     * Replace the icon in the button with one of the same contents but different dims
     *
     * @param width The new width value of the icon
     * @param height The new height value of the icon
     */
>>>>>>> main
    public void resizeIcon(double width, double height){

        // Recreate the icon
        StackPane icon = new UIImage(iconPath, width, height);

        getChildren().removeFirst();
        getChildren().addFirst(icon);

    }

}