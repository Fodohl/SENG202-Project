package seng202.team1.gui.components;

import seng202.team1.gui.GraphicsCommon;

public record BlockableUISet(

        UIKeyValuePair errorMessage,
        UISpinner loader,
        UIButton action
)
{

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

    public void showError(String message) {

        stopLoading();

        if(errorMessage != null) {
            GraphicsCommon.toggleClass(true, errorMessage, "error");
            errorMessage.setVisible(true);
            errorMessage.setText(message);
        }

    }
}
