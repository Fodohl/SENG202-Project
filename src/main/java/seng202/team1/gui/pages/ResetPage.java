package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.data.User;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UITextInput;
import seng202.team1.gui.components.UITitle;
import seng202.team1.state.AuthState;

import java.security.InvalidParameterException;

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

        UITitle title = new UITitle("Reset Password");
        email = new UITextInput("Enter email...");
        code = new UITextInput("Enter code...");
        password = new UITextInput("Enter password...");
        passwordConfirm = new UITextInput("Enter password again...");

        UIButton signUp = new UIButton("Reset", true);

        Label back = new Label("Go Back");
        back.getStyleClass().add("forgot");


        signUp.setOnAction(e -> handleReset());
        back.setOnMouseClicked(e -> handleBack());


        VBox form = new VBox(10, title, email, code, password, passwordConfirm, signUp, back);
        form.getStyleClass().add("auth-form");
        return form;
    }

     private void handleReset(){

        AuthState auth = router.getGlobalState().getAuth();;

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

     private void handleBack(){
        router.switchPage(Pages.LOGIN);
    }
}
