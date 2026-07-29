package seng202.team1.gui.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.core.ScreenManager;
import seng202.team1.gui.screens.ScreenType;


//@todo - abstract icons?
//@todo - screenmn ref to auto active (&refresh)

public class UINavButton extends HBox {

    private ScreenManager screenManager;
    private ScreenType dest;

    public UINavButton(ScreenManager screenManager, ScreenType dest) {

        // Setup state
        super();
        this.screenManager = screenManager;
        this.dest = dest;

        String iconPath = String.format("/images/%s.svg", dest.name().toLowerCase());
        String title = StringHelpers.toTitleCase(dest.name());

        // Apply styles
        getStyleClass().add("navitem");
        updateIsActive(screenManager.getCurrentScreen());

        // Add icon
        StackPane icon = new UIImage(iconPath, 25, 25);
        icon.getStyleClass().add("nav-icon");

        // Add text
        Label label = new Label(title);
        label.getStyleClass().add("nav-text");

        // Combine
        getChildren().addAll(icon, label);

        // Handle events
        setOnMouseClicked(e -> screenManager.showScreen(dest));
        screenManager.registerSceneChangeCallback(this::updateIsActive);
    }

    private void updateIsActive(ScreenType currentActive){

        // No active screen
        if(currentActive == null || dest == null)
            return;

        if(dest == currentActive){

            // Dont add if already added
            if(!getStyleClass().contains("active"))
                getStyleClass().add("active");
        } else{
            getStyleClass().remove("active");
        }
    }
}
