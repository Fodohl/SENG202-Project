package seng202.team1.gui.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.core.ScreenManager;
import seng202.team1.gui.screens.ScreenType;

/**
 * A UI Component to handle rendering navigation buttons
 *
 * Renders a button for a given screen type, expects a svg of the same name (lowercase) to exist for it's icon. This nav
 * button will react to navigation changes and indicate if it represents the active page.
 *
 * Note: No onClick needed, will automatically handle navigation calls
 *
 * @todo maybe abstract away screen manager into UIComponent?
 */
public class UINavButton extends HBox {

    /// The screen manager responsible for the active screen and switching to others
    private ScreenManager screenManager;

    /// The screen type this nav button represents as a clickable destination
    private ScreenType dest;

    /**
     * Constructs a new UINavButton
     *
     * @param screenManager The current screen manger for the screen this button is on
     * @param dest The navigation destination
     */
    public UINavButton(ScreenManager screenManager, ScreenType dest) {

        // Setup state
        super();
        this.screenManager = screenManager;
        this.dest = dest;

        // Extract info from screen type
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

    /**
     * Updates the style of this nav button to indicate whether this button's page is currently being shown or not
     *
     * @param currentActive The currently active screen
     */
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
