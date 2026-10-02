package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.types.user.Session;
import seng202.team1.types.user.User;

import java.security.InvalidParameterException;

import static seng202.team1.state.AuthState.*;
import static seng202.team1.state.AuthState.validatePassword;

/**
 * Page that displays the configuration menu of the app
 */
public class SettingsPage extends Page {


    private static final Logger log = LoggerFactory.getLogger(SettingsPage.class);
    private UITextInput userName;
    private UITextInput userEmail;

    private UITextInput userPasswordOld;
    private UITextInput userPasswordNew;
    private UITextInput userPasswordNewConfirm;

    private BlockableUISet updateFrontendUI;
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
        userName = new UITextInput("Enter your name...");
        userEmail = new UITextInput("Enter your email...");
        UISection userDetails = new UISection(detailsTitle, updateErrors, userName, userEmail);

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

        save.setOnAction(e -> saveChanges());
        cancel.setOnAction(e -> updateContents());
        deleteAccount.setOnAction(e -> delete());
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

        // Ensure button spacing
        saveSection.getStyleClass().add("buttons");
        manageButtons.getStyleClass().add("buttons");
        deleteAccount.getStyleClass().add("error");

        VBox root = new VBox(20, userDetails, passwordChange, saveSection, manageButtons);
        root.setAlignment(Pos.CENTER);
        return root;
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

            // Refresh app with new details
            getState().getAuth().updateUser(enteredName, enteredEmail);
            updateContents();

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

    }
}
