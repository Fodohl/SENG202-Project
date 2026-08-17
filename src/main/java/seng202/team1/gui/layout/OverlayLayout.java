package seng202.team1.gui.layout;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.WindowManager;

public class OverlayLayout extends LayoutElement {

    StackPane root = new StackPane();

    public OverlayLayout(WindowManager windowManager) {
        super(windowManager);

        // Background overlay
        VBox background = new VBox();
        background.getStyleClass().add("overlay");
        background.setOnMouseClicked(e -> windowManager.hideOverlay());

        root.getChildren().add(background);
    }

    @Override
    protected Parent renderSelf() {
        return root;
    }


    public void setVisible(boolean visible){
        root.setVisible(visible);
    }

    public void setContents(Parent contents){

        // Remove old contents
        while(root.getChildren().size() > 1)
            root.getChildren().removeLast();

        // Center the modal content over the overlay
        StackPane.setAlignment(contents, Pos.CENTER);
        root.getChildren().add(contents);
    }
}
