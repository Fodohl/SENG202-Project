package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class UIModal extends StackPane {

    public UIModal(String iconPath, String modalTitle, Node... children){

        // Background overlay
        VBox background = new VBox();
        background.getStyleClass().add("overlay");
        background.setOnMouseClicked(e -> hideModal());

        // Wrapper for children
        VBox body = new VBox(16);
        body.getStyleClass().add("body");
        body.getChildren().addAll(children);

        // Container holding the contents
        VBox contents = new VBox(header(iconPath, modalTitle), body);
        contents.getStyleClass().add("modal");

        // Center the modal content over the overlay
        this.getChildren().addAll(background, contents);
        StackPane.setAlignment(contents, Pos.CENTER);
    }

    private HBox header(String iconPath, String modalTitle){

        // Create the header elements
        UIImage titleIcon = new UIImage(iconPath, 50, 50);
        UITitle title = new UITitle(modalTitle);
        UIButton close = new UIButton("x");

        // Hide the modal on click
        close.getStyleClass().add("close");
        close.setOnAction(e -> hideModal());

        // Push the close button to the rights
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Combine the contents
        HBox headerContents = new HBox(titleIcon, title, spacer, close);
        headerContents.getStyleClass().add("header");
        headerContents.setAlignment(Pos.CENTER_LEFT);

        return headerContents;
    }

    public void hideModal(){
        setVisible(false);
    }
}
