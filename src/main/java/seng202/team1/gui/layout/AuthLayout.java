package seng202.team1.gui.layout;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIImage;

public class AuthLayout extends Layout {


    private VBox form = new VBox();
    private VBox root;

    public AuthLayout(Router router){
        super(router);
    }

    @Override
    protected Parent renderSelf() {

        // Logo
        HBox logo = new HBox(10);
        logo.getStyleClass().addAll("logo", "auth-logo");
        StackPane logoIcon = new UIImage("/media/images/logo.svg", 44, 44);

        // Title
        Label logoTitle = new Label("TrashApp");
        logoTitle.getStyleClass().add("logo-h1");
        logo.getChildren().addAll(logoIcon, logoTitle);

        root = new VBox(logo, form);
        root.setAlignment(Pos.TOP_CENTER);
        return root;

    }


    @Override
    public void swap(Parent rendered) {
        form = (VBox) rendered;
        root.getChildren().set(1, form);
    }
}
