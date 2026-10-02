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
import seng202.team1.state.AuthState;

import java.security.InvalidParameterException;

public class LoginPage extends Page{

    private UITextInput email;
    private UITextInput password;
    private UIButton login;

    private BlockableUISet frontend;

    public LoginPage(Router router) {

        super(router, Pages.LOGIN);
    }

    @Override
    protected Parent renderSelf() {


        UITitle title = new UITitle("Login");
        email = new UITextInput("Enter email...");
        password = new UITextInput("Enter password...");

        // Hide input
        password.setPrivate();

        login = new UIButton("Login", true);
        UIButton signUp = new UIButton("Sign Up", true);

        Label forgot = new Label("Forgot password?");
        forgot.getStyleClass().add("forgot");
        login.getStyleClass().add("action");

        login.setOnAction(e -> handleLogin());
        signUp.setOnAction(e -> handleSignUp());
        forgot.setOnMouseClicked(e -> handleForgot());

        // Error handling
        UIKeyValuePair error = new UIKeyValuePair("Error", "");
        error.setVisible(false);
        frontend = new BlockableUISet(error, null, login);

        VBox form = new VBox(10, title, error, email, password, login, signUp, forgot);
        form.getStyleClass().add("auth-form");
        return form;
    }

    private void handleLogin(){

        AuthState auth = router.getGlobalState().getAuth();;

        String enteredEmail = email.getInput();
        String enteredPassword = password.getInput();

        // Clear previous errors
        email.setError("");
        password.setError("");

        // Validate
        try{
            auth.validateEmail(enteredEmail);
        } catch (InvalidParameterException e) {
            email.setError(e.getMessage());
        }

        try{
            auth.validatePassword(enteredPassword);
        } catch (InvalidParameterException e) {
            password.setError(e.getMessage());
        }

        // Cant login with these credentials
        if(email.isErroring() || password.isErroring())
            return;

        // Login
        auth.login(enteredEmail, enteredPassword, frontend);
    }

     private void handleSignUp(){
        router.switchPage(Pages.SIGN_UP);
    }

     private void handleForgot(){
        router.switchPage(Pages.RESET);
    }

    @Override
    public void updateContents() {

        // Already logged
        if(getState().getAuth().getSession() != null)
            router.switchPhase(Phases.MAIN);

    }
}
