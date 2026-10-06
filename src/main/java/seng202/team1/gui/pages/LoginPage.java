package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.gui.phases.Phases;
import seng202.team1.state.AuthState;

import java.security.InvalidParameterException;

/**
 * Page that displays a login form
 */
public class LoginPage extends Page{

    /// Where the user enters their email
    private UITextInput email;

    /// Where the user enters their email
    private UITextInput password;

    /// The button to trigger a login
    private UIButton login;

    /// The UI component set to manage whilst waiting for requests to complete
    private BlockableUISet frontend;

    /// Whether the session should be persisted after login
    private CheckBox rememberMe;

    /**
     * Constructs a new LoginPage
     *
     * @param router the router that handles this page
     */
    public LoginPage(Router router) {

        super(router, Pages.LOGIN);
    }

    /**
     * Constructs the UI for the login page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {


        UITitle title = new UITitle("Login");
        email = new UITextInput("Enter email...");
        password = new UITextInput("Enter password...");

        // Hide input
        password.setPrivate();

        login = new UIButton("Login", true);
        UIButton signUp = new UIButton("Sign Up", true);

        login.setOnAction(e -> handleLogin());
        signUp.setOnAction(e -> handleSignUp());

        // Assume enter keypress in a password is same as login press
        password.setOnAction(e -> handleLogin());

        // Error handling
        UIKeyValuePair error = new UIKeyValuePair("Error", "");
        error.setVisible(false);
        frontend = new BlockableUISet(error, null, login);

        rememberMe = new CheckBox("Remember me");

        VBox form = new VBox(10, title, error, email, password, rememberMe, login, signUp);
        form.getStyleClass().add("auth-form");
        return form;
    }

    /**
     * Verifies the user's input and sends a login request, erroring if invalid
     */
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
        auth.login(enteredEmail, enteredPassword, rememberMe.isSelected(), frontend);
    }

    /**
     * Switch to the sign up page
     */
     private void handleSignUp(){
        router.switchPage(Pages.SIGN_UP);
    }

    /**
     * Checks if the user already has a saved sesssion and skips login if so
     */
    @Override
    public void updateContents() {

        // Already logged
        if(getState().getAuth().getSession() != null)
            router.switchPhase(Phases.MAIN);

    }
}
