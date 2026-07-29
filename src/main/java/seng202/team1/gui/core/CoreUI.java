package seng202.team1.gui.core;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UINavButton;
import seng202.team1.gui.screens.ScreenType;

import java.util.Objects;

import static seng202.team1.gui.GraphicsCommon.precentX;

public class CoreUI extends LayoutElement {

    public CoreUI(ScreenManager screenManager) {
        super(screenManager, null);

    }

    private Parent sidebar() {

        // Logo
        HBox logo = new HBox(10);
        logo.getStyleClass().add("logo");
        StackPane logoIcon = new UIImage("/images/logo.svg", 44, 44);

        // Title
        Label logoTitle = new Label("TrashApp");
        logoTitle.getStyleClass().add("logo-h1");
        logo.getChildren().addAll(logoIcon, logoTitle);

        // Navigation Menu
        VBox navbar = new VBox(8);
        for (ScreenType option : ScreenType.values())
            if(option != ScreenType.SETTINGS)
                navbar.getChildren().add(new UINavButton(screenManager, option));

        // Settings at the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        HBox navSettings = new UINavButton(screenManager, ScreenType.SETTINGS);

        // Combine
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(precentX(0.15));
        sidebar.getChildren().addAll(logo, navbar, spacer, navSettings);
        return sidebar;

    }

    private Parent header() {
        return null;
    }

    @Override
    protected Parent render_self() {

        // Root
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        root.setLeft(sidebar());
//        root.setCenter(page);
        return root;

    }
}
