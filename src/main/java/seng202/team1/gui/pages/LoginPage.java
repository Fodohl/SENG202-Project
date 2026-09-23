package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;

public class LoginPage extends Page{

    private UITextInput email;
    private UITextInput password;

    public LoginPage(Router router) {

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

        UITitle title = new UITitle("Login");
        email = new UITextInput("Enter email...");
        password = new UITextInput("Enter password...");

        UIButton login = new UIButton("Login", true);
        UIButton signUp = new UIButton("Sign Up", true);

        Label forgot = new Label("Forgot password?");
        forgot.getStyleClass().add("forgot");
        login.getStyleClass().add("action");

        VBox form = new VBox(10, title, email, password, login, signUp, forgot);
        form.getStyleClass().add("auth-form");

        VBox root = new VBox(logo, form);
        root.setAlignment(Pos.TOP_CENTER);
        return root;

    }
}
