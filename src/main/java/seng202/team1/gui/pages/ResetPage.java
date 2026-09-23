package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UITextInput;
import seng202.team1.gui.components.UITitle;

public class ResetPage extends Page{

    private UITextInput email;
    private UITextInput code;
    private UITextInput password;
    private UITextInput passwordConfirm;

    public ResetPage(Router router) {

        super(router, Pages.LOGIN);
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

        UITitle title = new UITitle("Reset Password");
        email = new UITextInput("Enter email...");
        code = new UITextInput("Enter code...");
        password = new UITextInput("Enter password...");
        passwordConfirm = new UITextInput("Enter password again...");

        UIButton signUp = new UIButton("Reset", true);

        Label back = new Label("Go Back");
        back.getStyleClass().add("forgot");

        VBox form = new VBox(10, title, email, code, password, passwordConfirm, signUp, back);
        form.getStyleClass().add("auth-form");

        VBox root = new VBox(logo, form);
        root.setAlignment(Pos.TOP_CENTER);
        return root;

    }
}
