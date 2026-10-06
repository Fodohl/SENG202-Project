package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.types.user.Session;
import seng202.team1.types.user.User;

import java.security.InvalidParameterException;

import static seng202.team1.state.AuthState.*;

/**
 * Page that displays the configuration menu of the app
 */
public class SettingsPage extends Page {


    private static final Logger log = LoggerFactory.getLogger(SettingsPage.class);
    private UITextInput userName;
    private UITextInput userEmail;
    private UIAvatar avatar;

    private UITextInput userPasswordOld;
    private UITextInput userPasswordNew;
    private UITextInput userPasswordNewConfirm;

    /// The UI component set to manage whilst waiting for update requests to complete
    private BlockableUISet updateFrontendUI;

    /// The UI component set to manage whilst waiting for delete requests to complete
    private BlockableUISet deleteFrontendUI;

    private User user;

    /**
     * Constructs a new SettingsPage
     *
     * @param router the window this page is on
     */
    public SettingsPage(Router router) {

        super(router, Pages.SETTINGS);

    }

    /**
     * Constructs the UI for the settings page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        UITitle detailsTitle = new UITitle("User Details");
        UIKeyValuePair updateErrors = new UIKeyValuePair("Error", "");
        UIKeyValuePair iconErrors = new UIKeyValuePair("Error", "");
        avatar = new UIAvatar(user, 150);
        userName = new UITextInput("Enter your name...");
        userEmail = new UITextInput("Enter your email...");

        // Combine and style the avatar spot
        VBox details = new VBox(userName, userEmail);
        HBox.setHgrow(details, Priority.ALWAYS);
        UISection userDetails = new UISection(detailsTitle, iconErrors, new HBox(50, avatar, details), updateErrors);

        UITitle changePasswordTitle = new UITitle("Change Password");
        UIKeyValuePair passwordErrors = new UIKeyValuePair("Error", "");
        userPasswordOld = new UITextInput("Enter your current password...");
        userPasswordNew = new UITextInput("Enter your new password...");
        userPasswordNewConfirm = new UITextInput("Enter your new password again...");
        UISection passwordChange = new UISection(changePasswordTitle, passwordErrors, userPasswordOld, userPasswordNew, userPasswordNewConfirm);

        UIButton save = new UIButton("Save Changes");
        UIButton cancel = new UIButton("Cancel");
        UISection saveSection = new UISection(15, save, cancel);

        UIButton logOut = new UIButton("Log Out");
        UIButton deleteAccount = new UIButton("Delete Account");
        UISection manageButtons = new UISection(15, logOut, deleteAccount);

        // Assume enter keypress in a password is same as login press
        userPasswordNewConfirm.setOnAction(e -> saveChanges());

        save.setOnAction(e -> saveChanges());
        cancel.setOnAction(e -> updateContents());
        deleteAccount.setOnAction(e -> {
            if (confirm("Delete Account", "Are you sure you want to delete your account? This cannot be undone."))
            {
                delete();
            }
        });
        logOut.setOnAction(e -> logOut());

        // Hide input
        userPasswordOld.setPrivate();
        userPasswordNew.setPrivate();
        userPasswordNewConfirm.setPrivate();

        updateFrontendUI = new BlockableUISet(updateErrors, null, save);
        deleteFrontendUI = new BlockableUISet(passwordErrors, null, save);

        // Hide errors until needed
        updateErrors.setVisible(false);
        passwordErrors.setVisible(false);
        iconErrors.setVisible(false);

        // Ensure button spacing
        saveSection.getStyleClass().add("buttons");
        manageButtons.getStyleClass().add("buttons");
        deleteAccount.getStyleClass().add("error");

        // Fix visual weight
        avatar.setStyle(avatar.getStyle() + "-fx-translate-y:  -15px;");

        VBox root = new VBox(20, userDetails, passwordChange, saveSection, manageButtons);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    /**
     * Show a confirmation dialog
     *
     * @param title The dialog title
     * @param message The question to ask the user
     * @return True if OK is pressed
     */
    private boolean confirm(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(router.getStage());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setResizable(false);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(360);

        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    private void delete() {

        getState().getTaServer().deleteUser(s -> {

            // Session now invalid
            logOut();

        }, deleteFrontendUI);
    }

    private void saveChanges() {

        if (user == null)
            return;
        if (!confirm("Save Changes", "Are you sure you want to update your account details?"))
            return;

        String enteredName = userName.getInput();
        String enteredEmail = userEmail.getInput();
        String enteredPassword = userPasswordOld.getInput();
        String enteredPasswordNew = userPasswordNew.getInput();
        String enteredPasswordNewConfirmed = userPasswordNewConfirm.getInput();

        // Only saved if changed
        if (!enteredName.equals(user.name()) || !enteredEmail.equals(user.email()))
            updateDeatils(enteredName, enteredEmail);

        if (!enteredPassword.isEmpty())
            changePassword(enteredPassword, enteredPasswordNew, enteredPasswordNewConfirmed);


    }

    private void updateDeatils(String enteredName, String enteredEmail) {

        // Validate
        try {
            validateName(enteredName);
        } catch (InvalidParameterException e) {
            userName.setError(e.getMessage());
        }

        try {
            validateEmail(enteredEmail);
        } catch (InvalidParameterException e) {
            userEmail.setError(e.getMessage());
        }

        // Cant update with these credentials
        if (userName.isErroring() || userEmail.isErroring())
            return;

        getState().getTaServer().updateUser(enteredName, enteredEmail, s -> {

            // Refresh whole app with new details
            getState().getAuth().updateUser(enteredName, enteredEmail);
            router.refreshPage(pageType);

        }, updateFrontendUI);
    }

    private void changePassword(String enteredPassword, String enteredPasswordNew, String enteredPasswordNewConfirmed) {

        // Validate
        try {
            validatePassword(enteredPassword);
        } catch (InvalidParameterException e) {
            userPasswordOld.setError(e.getMessage());
        }

        try {
            validatePassword(enteredPasswordNew);
        } catch (InvalidParameterException e) {
            userPasswordNew.setError(e.getMessage());
        }

        try {
            validatePassword(enteredPasswordNewConfirmed);
        } catch (InvalidParameterException e) {
            userPasswordNewConfirm.setError(e.getMessage());
        }

        if (!enteredPasswordNew.equals(enteredPasswordNewConfirmed))
            userPasswordNewConfirm.setError("passwords do not match");

        // Cant update with these credentials
        if (userPasswordOld.isErroring() || userPasswordNew.isErroring() || userPasswordNewConfirm.isErroring())
            return;

        getState().getTaServer().updatePassword(enteredPassword, enteredPasswordNew, s -> {

            // Clear form
            userPasswordOld.clear();
            userPasswordNew.clear();
            userPasswordNewConfirm.clear();


        }, deleteFrontendUI);
    }

    private void logOut() {
        getState().getAuth().logOut();
    }

    @Override
    public void updateContents() {

        Session session = getState().getAuth().getSession();

        // Not logged in
        if (session == null)
            return;

        user = session.userDetails();
        userName.setInput(user.name());
        userEmail.setInput(user.email());
        avatar.setUser(user);
    }
}
