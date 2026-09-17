package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIButton;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UITitle;
import seng202.team1.gui.layout.LayoutElement;
import seng202.team1.gui.pages.Pages;

//@todo abstract away the simaliries between this and page?

/**
 * A Modal to display overlay contents
 */
public abstract class Modal extends LayoutElement implements Cloneable {

    /// The type of modal this UI component represents
    private Modals modalType;

    /// The window ID for this modal (if pop up modal)
    private long modalWindow = -1;

       /**
     * Constructs a new Modal
     *
    * @param windowManager The window this modal is on
     * @param modalType The type of modal this is
     */
    public Modal(WindowManager windowManager, Modals modalType){

        super(windowManager);

        // Register handler for page changes
        this.modalType = modalType;
        this.windowManager.registerModalChangeCallback(this::onModalChange);
    }

    abstract Parent buildBody();

    /**
     * Render the base layout for the modal with the contents of subclasses composited inside
     *
     * @return The full rendered modal
     */
    @Override
    protected Parent renderSelf() {


        // Wrapper for children
        ScrollPane body = new ScrollPane(buildBody());
        body.getStyleClass().add("body");

        // Only scrol verticaly
        body.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        body.setFitToWidth(true);

        // Container holding the contents
        VBox contents = new VBox(header(modalType), body);
        contents.getStyleClass().add("modal");

        return contents;
    }

    /**
     * Render the header text of the modal
     *
     * @param modalType The modal type, to be used as a title
     * @return The rendered header
     */
    private HBox header(Modals modalType){

        String iconPath = String.format("/media/images/%s.svg", modalType.name().toLowerCase());
        String modalTitle = StringHelpers.enumNameToTitle(modalType.name());

        // Create the header elements
        UIImage titleIcon = new UIImage(iconPath, 50, 50);
        UITitle title = new UITitle(modalTitle);
        UIButton close = new UIButton("x");

        // Hide the modal on click
        close.getStyleClass().add("close");
        close.setOnAction(e -> closeModal());

        // Push the close button to the rights
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Combine the contents
        HBox headerContents = new HBox(titleIcon, title, spacer, close);
        headerContents.getStyleClass().add("header");
        headerContents.setAlignment(Pos.CENTER_LEFT);

        return headerContents;
    }

    /**
     * Stop displaying the modal
     */
    public void closeModal(){

        if(modalWindow == -1)
            windowManager.closeModal();
        else
            windowManager.killModal(modalWindow);
    }


    /**
     * Update the contents of the modal without performing a full re-render
     */
    protected void updateContents(){
        // overridden only if needed
    }

    /**
     * Updates the modal contents when this modal is about to be shown
     *
     * @param destModal The modal that has been switched to
     */
    private void onModalChange(Modals destModal){

        // Dont rerender if not being shown
        if(modalType != destModal)
            return;

        // Update self
        updateContents();
    }

    /**
     * Set the ID of the popup window the modal is shown in
     *
     * @param windowID The window's ID
     */
    public void setWindowed(long windowID) {
        modalWindow = windowID;
    }

    /**
     * Create a new instance of the modal to be shown in a seperate popup window
     *
     * @return A new modal instance
     */
    @Override
    public Modal clone() {
        Modal clone = (Modal) super.clone();
        clone.modalWindow = -1;
        return clone;
    }

    public void recvData(Object data) {
        // overridden only if needed
    }
}
