package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.types.User;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UITextInput;
import seng202.team1.gui.components.UITitle;
import seng202.team1.state.AuthState;

import java.security.InvalidParameterException;
import java.time.Instant;

public class SignUpPage extends Page {

    private UITextInput name;
    private UITextInput email;
    private UITextInput password;
    private UITextInput passwordConfirm;

    public SignUpPage(Router router) {

        super(router, Pages.LOGIN);
    }

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

        signUp.setOnAction(e -> handleSignUp());
        back.setOnMouseClicked(e -> handleBack());

        VBox form = new VBox(10, title, name, email, password, passwordConfirm, signUp, back);
        form.getStyleClass().add("auth-form");
        return form;
    }


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
            auth.validateName(enteredName);
        } catch (InvalidParameterException e) {
            name.setError(e.getMessage());
        }

        try {
            auth.validateEmail(enteredEmail);
        } catch (InvalidParameterException e) {
            email.setError(e.getMessage());
        }

        try {
            auth.validatePassword(enteredPassword);
        } catch (InvalidParameterException e) {
            password.setError(e.getMessage());
        }

        try {
            auth.validatePassword(enteredPasswordConfirmed);
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
        auth.signUp(newUser, enteredPassword);
    }

    private void handleBack() {
        router.switchPage(Pages.LOGIN);
    }
}
