package seng202.team1.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.GraphicsCommon;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.pages.Pages;

/**
 * A UI Component to handle rendering navigation buttons
 *
 * Renders a button for a given page type, expects a svg of the same name (lowercase) to exist for it's icon. This nav
 * button will react to navigation changes and indicate if it represents the active page.
 *
 * Note: No onClick needed, will automatically handle navigation calls
 */
public class UINavButton extends HBox {

    /// The window manager responsible for the active page and switching to others
    private WindowManager windowManager;

    /// The page type this nav button represents as a clickable destination
    private Pages dest;

    /**
     * Constructs a new UINavButton
     *
     * @param windowManager The current window manger for the page this button is on
     * @param dest The navigation destination
     */
    public UINavButton(WindowManager windowManager, Pages dest) {

        // Setup state
        super();
        this.windowManager = windowManager;
        this.dest = dest;

        // Extract info from page type
        String iconPath = String.format("/images/%s.svg", dest.name().toLowerCase());
        String title = StringHelpers.toTitleCase(dest.name());

        // Apply styles
        getStyleClass().add("navitem");
        updateIsActive(windowManager.getCurrentPage());

        // Add icon
        StackPane icon = new UIImage(iconPath, 25, 25);
        icon.getStyleClass().add("nav-icon");

        // Add text
        Label label = new Label(title);
        label.getStyleClass().add("nav-text");

        // Combine
        getChildren().addAll(icon, label);

        // Handle events
        setOnMouseClicked(e -> windowManager.switchPage(dest));
        windowManager.registerPageChangeCallback(this::updateIsActive);
    }

    /**
     * Updates the style of this nav button to indicate whether this button's page is currently being shown or not
     *
     * @param currentActive The currently active page
     */
    private void updateIsActive(Pages currentActive){

        // No active page
        if(currentActive == null || dest == null)
            return;

        // Add styling accordingly
        GraphicsCommon.toggleClass(dest == currentActive, this, "active");
    }
}
