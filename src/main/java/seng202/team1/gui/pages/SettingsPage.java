package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.gui.phases.Phases;

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

    /**
     * Constructs a new SettingsPage
     *
     * @param router the window this page is on
     */
   public SettingsPage(Router router){

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
       userName = new UITextInput("Enter your name...");
       userEmail = new UITextInput("Enter your email...");
       UISection userDetails = new UISection(detailsTitle, userName, userEmail);

       UITitle changePasswordTitle = new UITitle("Change Password");
       userPasswordOld = new UITextInput("Enter your current password...");
       userPasswordNew = new UITextInput("Enter your new password...");
       userPasswordNewConfirm = new UITextInput("Enter your new password again...");
       UISection passwordChange = new UISection(changePasswordTitle, userPasswordOld, userPasswordNew, userPasswordNewConfirm);

       UIButton save = new UIButton("Save Changes");
       UIButton cancel = new UIButton("Cancel");
       UISection saveSection = new UISection(15, save, cancel);

       UIButton logOut = new UIButton("Log Out");
       UIButton deleteAccount = new UIButton("Delete Account");
       UISection manageButtons = new UISection(15, logOut, deleteAccount);

       save.setOnAction(e -> saveChanges());
       logOut.setOnAction(e -> logOut());

       // Hide input
       userPasswordOld.setPrivate();
       userPasswordNew.setPrivate();
       userPasswordNewConfirm.setPrivate();

       // Ensure button spacing
       saveSection.getStyleClass().add("buttons");
       manageButtons.getStyleClass().add("buttons");
       deleteAccount.getStyleClass().add("error");

       VBox root = new VBox(20, userDetails, passwordChange, saveSection, manageButtons);
       root.setAlignment(Pos.CENTER);
       return root;
    }

    private void saveChanges() {

    }

    private void logOut() {
       getState().getAuth().logOut();
    }
}
