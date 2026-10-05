package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.gui.components.*;
import seng202.team1.types.user.User;
import seng202.team1.gui.Router;
import seng202.team1.state.AuthState;

import java.security.InvalidParameterException;
import java.time.Instant;

import static seng202.team1.state.AuthState.*;

/**
 * Page that displays a sign up form
 */
public class SignUpPage extends Page {

    /// Where the user enters their name
    private UITextInput name;

    /// Where the user enters their email
    private UITextInput email;

    /// Where the user enters their password
    private UITextInput password;

    /// Where the user re-enters their password
    private UITextInput passwordConfirm;

    /// The UI component set to manage whilst waiting for requests to complete
    private BlockableUISet frontend;

    /**
     * Constructs a new SignUpPage
     *
     * @param router the router that handles this page
     */
    public SignUpPage(Router router) {

        super(router, Pages.SIGN_UP);
    }

    /**
     * Constructs the UI for the sign up page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        UITitle title = new UITitle("Sign Up");
        name = new UITextInput("Enter name...");
        email = new UITextInput("Enter email...");
        password = new UITextInput("Enter password...");
        passwordConfirm = new UITextInput("Enter password again...");

        // Hide input
        password.setPrivate();
        passwordConfirm.setPrivate();

        UIButton signUp = new UIButton("Sign Up", true);

        Label back = new Label("Go Back");
        back.getStyleClass().add("forgot");

        // Assume enter keypress in a password is same as login press
        passwordConfirm.setOnAction(e -> handleSignUp());

        signUp.setOnAction(e -> handleSignUp());
        back.setOnMouseClicked(e -> handleBack());

        // Error handling
        UIKeyValuePair error = new UIKeyValuePair("Error", "");
        error.setVisible(false);
        frontend = new BlockableUISet(error, null, signUp);

        VBox form = new VBox(10, title, error, name, email, password, passwordConfirm, signUp, back);
        form.getStyleClass().add("auth-form");
        return form;
    }

    /**
     * Verifies the user's input and sends a sign up request, erroring if invalid
     */
    private void handleSignUp() {

        AuthState auth = router.getGlobalState().getAuth();

        String enteredName = name.getInput();
        String enteredEmail = email.getInput();
        String enteredPassword = password.getInput();
        String enteredPasswordConfirmed = passwordConfirm.getInput();

        // Clear previous errors
        name.setError("");
        email.setError("");
        password.setError("");
        passwordConfirm.setError("");

        // Validate
        try {
            validateName(enteredName);
        } catch (InvalidParameterException e) {
            name.setError(e.getMessage());
        }

        try {
            validateEmail(enteredEmail);
        } catch (InvalidParameterException e) {
            email.setError(e.getMessage());
        }

        try {
            validatePassword(enteredPassword);
        } catch (InvalidParameterException e) {
            password.setError(e.getMessage());
        }

        try {
            validatePassword(enteredPasswordConfirmed);
        } catch (InvalidParameterException e) {
            passwordConfirm.setError(e.getMessage());
        }

        if (!enteredPassword.equals(enteredPasswordConfirmed))
            passwordConfirm.setError("passwords do not match");

        // Cant login with these credentials
        if (name.isErroring() || email.isErroring() || password.isErroring() || passwordConfirm.isErroring())
            return;

        // Signup
        User newUser = new User(0, Instant.now(), Instant.now(), enteredName, enteredEmail, "", 0);
        auth.signUp(newUser, enteredPassword, frontend);
    }

    /**
     * Go back to the login page
     */
    private void handleBack() {
        router.switchPage(Pages.LOGIN);
    }
}
