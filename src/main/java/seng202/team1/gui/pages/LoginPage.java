package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.gui.phases.Phases;

public class LoginPage extends Page{

    private UITextInput email;
    private UITextInput password;

    public LoginPage(Router router) {

        super(router, Pages.LOGIN);
    }

    @Override
    protected Parent renderSelf() {


        UITitle title = new UITitle("Login");
        email = new UITextInput("Enter email...");
        password = new UITextInput("Enter password...");

        UIButton login = new UIButton("Login", true);
        UIButton signUp = new UIButton("Sign Up", true);

        Label forgot = new Label("Forgot password?");
        forgot.getStyleClass().add("forgot");
        login.getStyleClass().add("action");

        login.setOnAction(e -> handleLogin());
        signUp.setOnAction(e -> handleSignUp());
        forgot.setOnMouseClicked(e -> handleForgot());

        VBox form = new VBox(10, title, email, password, login, signUp, forgot);
        form.getStyleClass().add("auth-form");
        return form;
    }

    private void handleLogin(){
        router.switchPhase(Phases.MAIN);
    }

     private void handleSignUp(){
        router.switchPage(Pages.SIGN_UP);
    }

     private void handleForgot(){
        router.switchPage(Pages.RESET);
    }
}
