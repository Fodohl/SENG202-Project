package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.GraphicsCommon;


public abstract interface UIInputComponent {

    /// UIInputComponent specific logger
    static final Logger log = LogManager.getLogger(UIInputComponent.class);

    String errorText = "";

    Object getInput();

    void setInput(Object value);

    boolean hasInput();

    void clear();

    void setError(String isError);

    boolean isErroring();

    default boolean isErroringInternal(Label errorDisplay){

        if(errorDisplay == null)
            return false;

        return !errorDisplay.getText().isEmpty();
    }

     default void setIsErrorInternal(String errorText, Node thisNode, Label errorDisplay){

        // Clearing error
        if(errorText.isEmpty()){
            errorDisplay.setText(errorText);
            return;
        }

        // Hide if not erroring
        boolean isErroring = isErroring();
        errorDisplay.setVisible(isErroring);

        // Log
        errorDisplay.setText(String.format("Error: %s", errorText));
        if(isErroring)
            log.error(errorText);

        // Style
        GraphicsCommon.toggleClass(isErroring, errorDisplay, "error");
        GraphicsCommon.toggleClass(isErroring, thisNode, "error");
    }

}
