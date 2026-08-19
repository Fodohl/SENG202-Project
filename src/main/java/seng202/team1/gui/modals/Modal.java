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

public abstract class Modal extends LayoutElement implements Cloneable {

    private Modals modalType;

    private long modalWindow = -1;

    public Modal(WindowManager windowManager, Modals modalType){

        super(windowManager);

        // Register handler for page changes
        this.modalType = modalType;
        this.windowManager.registerModalChangeCallback(this::onModalChange);
    }

    abstract Parent buildBody();

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

    private HBox header(Modals modalType){

        String iconPath = String.format("/images/%s.svg", modalType.name().toLowerCase());
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

    public void setWindowed(long windowID) {
        modalWindow = windowID;
    }

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
