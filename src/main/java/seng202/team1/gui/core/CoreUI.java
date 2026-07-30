package seng202.team1.gui.core;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
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

    private Pane content = null;

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

        // Search images
        UIImage searchIcon = new UIImage("/images/search.svg", 16, 16);

        // Search text
        TextField searchInput = new TextField();
        searchInput.setPromptText("Enter address...");
        searchInput.getStyleClass().add("search-input");

        // Build Search Bar
        HBox searchBar = new HBox(searchIcon, searchInput);
        searchBar.getStyleClass().add("search-bar");

        // Account
        Label accountLabel = new Label("Account (DB)");
        accountLabel.getStyleClass().add("account-text");
        BorderPane.setAlignment(accountLabel, Pos.CENTER_RIGHT);

        // Combine
        BorderPane header = new BorderPane();
        header.getStyleClass().add("header");
        header.setPrefHeight(60);
        header.setRight(accountLabel);
        header.setLeft(searchBar);

        return header;
    }

    @Override
    protected Parent render_self() {

        // Debug content
        content = new StackPane();
        content.getStyleClass().add("content");
        Label contentText = new Label("Content");
        content.getChildren().add(contentText);

        // Page
        BorderPane page = new BorderPane();
        page.getStyleClass().add("page");
        page.setTop(header());
        page.setCenter(content);

        // Root
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        root.setLeft(sidebar());
        root.setCenter(page);
        return root;
    }
}
