package seng202.team1.gui.components;

import seng202.team1.gui.GraphicsCommon;

/**
 * A group of optional UI components that are to be blocked whilst an operation is happening
 *
 * @param errorMessage The component to show any operational errors on
 * @param loader The spinner to show whilst waiting for completion
 * @param action The action button that triggered the operation to disable whilst waiting
 */
public record BlockableUISet(

        UIKeyValuePair errorMessage,
        UISpinner loader,
        UIButton action
)
{

    /**
     * Set the components into their loading state
     */
    public void startLoading(){

        if(errorMessage != null){
            errorMessage.setText("");
            errorMessage.setVisible(false);
        }

        if(loader != null)
            loader.setVisible(true);

        if(action != null)
            action.setSpinner(true);

    }

    /**
     * Set the components into the normal state
     */
    public void stopLoading(){

        if(errorMessage != null){
            errorMessage.setText("");
            errorMessage.setVisible(false);
        }

        if(loader != null)
            loader.setVisible(false);

        if(action != null)
            action.setSpinner(false);

    }

    /**
     * Set the components into the error state. Will clear loading state
     *
     * @param message The error that occurred
     */
    public void showError(String message) {

        stopLoading();

        if(errorMessage != null) {
            GraphicsCommon.toggleClass(true, errorMessage, "error");
            errorMessage.setVisible(true);
            errorMessage.setText(message);
        }

    }
}
