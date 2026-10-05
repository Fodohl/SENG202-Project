package seng202.team1.gui.pages;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UITextInput;
import seng202.team1.gui.components.UITitle;
import seng202.team1.state.AuthState;

import java.security.InvalidParameterException;

/**
 * Page that displays a password reset form
 */
public class ResetPage extends Page {

    /// Where the user enters their email
    private UITextInput email;

    /// Where the user enters their reset code
    private UITextInput code;

    /// Where the user enters their new password
    private UITextInput password;

    /// Where the user re-enters their new password
    private UITextInput passwordConfirm;

    /**
     * Constructs a new ResetPage
     *
     * @param router the router that handles this page
     */
    public ResetPage(Router router) {

        super(router, Pages.RESET);
    }


    /**
     * Constructs the UI for the reset page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        UITitle title = new UITitle("Reset Password");
        email = new UITextInput("Enter email...");
        code = new UITextInput("Enter code...");
        password = new UITextInput("Enter password...");
        passwordConfirm = new UITextInput("Enter password again...");

        UIButton signUp = new UIButton("Reset", true);

        Label back = new Label("Go Back");
        back.getStyleClass().add("forgot");

        // Assume enter keypress in a password is same as login press
        passwordConfirm.setOnAction(e -> handleReset());

        signUp.setOnAction(e -> handleReset());
        back.setOnMouseClicked(e -> handleBack());

        VBox form = new VBox(10, title, email, code, password, passwordConfirm, signUp, back);
        form.getStyleClass().add("auth-form");
        return form;
    }

    /**
     * Verifies the user's input and sends a reset request, erroring if invalid
     */
    private void handleReset() {

        AuthState auth = router.getGlobalState().getAuth();
        ;

        String enteredEmail = email.getInput();
        String enteredCode = code.getInput();
        String enteredPassword = password.getInput();
        String enteredPasswordConfirmed = passwordConfirm.getInput();

        // Clear previous errors
        code.setError("");
        email.setError("");
        password.setError("");
        passwordConfirm.setError("");

        Integer parsedCode = null;

        // Validate
        try {
            parsedCode = auth.validateCode(enteredCode);
        } catch (InvalidParameterException e) {
            code.setError(e.getMessage());
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

        if (password != passwordConfirm)
            passwordConfirm.setError("passwords do not match");

        // Cant login with these credentials
        if (code.isErroring() || email.isErroring() || password.isErroring() || passwordConfirm.isErroring())
            return;

        // Signup
        auth.resetPassword(enteredEmail, enteredCode, enteredPassword);
    }

    /**
     * Go back to the login page
     */
    private void handleBack() {
        router.switchPage(Pages.LOGIN);
    }
}
