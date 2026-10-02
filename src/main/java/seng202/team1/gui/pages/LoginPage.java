package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import seng202.team1.data.User;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UIPasswordInput;
import seng202.team1.gui.components.UISection;
import seng202.team1.gui.components.UITextInput;

public class LoginPage extends Page {
    private UITextInput emailInput;
    private UIPasswordInput passwordInput;
    private Label loginError;

    public LoginPage(WindowManager windowManager) {
        super(windowManager, Pages.LOGIN);
    }

    @Override
    protected Parent renderSelf() {

        Label title = new Label("TrashApp");
        title.getStyleClass().add("login-title");

        Label subtitle = new Label("Log in to continue");
        subtitle.getStyleClass().add("login-subtitle");

        emailInput = new UITextInput("Email");
        passwordInput = new UIPasswordInput("Password");

        loginError = new Label();
        loginError.getStyleClass().add("input-error");
        loginError.setVisible(false);
        loginError.setManaged(false);

        UIButton loginButton = new UIButton("Log In");

        loginButton.setOnMouseClicked(event -> attemptLogin());

        UISection form = new UISection(
                15,
                emailInput,
                passwordInput,
                loginError,
                loginButton
        );

        VBox container = new VBox(
                20,
                title,
                subtitle,
                form
        );

        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("login-page");

        return container;
    }

    private void attemptLogin() {

        clearErrors();

        if (!emailInput.hasInput()) {
            emailInput.setError("Email is required");
            return;
        }

        if (!passwordInput.hasInput()) {
            passwordInput.setError("Password is required");
            return;
        }

        String email = emailInput.getInput().toString();
        String password = passwordInput.getInput();

        User user = mockLogin(email, password);

        if (user == null) {
            loginError.setText("Incorrect email or password");
            loginError.setVisible(true);
            loginError.setManaged(true);
            return;
        }

        windowManager
                .getGlobalState()
                .setCurrentUser(user);

        windowManager.switchPage(Pages.DASHBOARD);
    }

    /**
     * Temporary authentication until TrashDB login is implemented.
     */
    private User mockLogin(String email, String password) {

        if (email.equalsIgnoreCase("connor@test.com")
                && password.equals("password")) {

            return new User(1, "Connor");
        }

        if (email.equalsIgnoreCase("archie@test.com")
                && password.equals("password")) {

            return new User(2, "Archie");
        }

        return null;
    }

    private void clearErrors() {

        passwordInput.clearError();

        loginError.setText("");
        loginError.setVisible(false);
        loginError.setManaged(false);
    }

    @Override
    public void updateContents() {
        // Nothing currently needs refreshing.
    }
}
