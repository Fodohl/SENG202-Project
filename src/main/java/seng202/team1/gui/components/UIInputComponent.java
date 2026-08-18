package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.GraphicsCommon;


public abstract interface UIInputComponent {

    /// UIInputComponent specific logger
    static final Logger log = LogManager.getLogger(UIInputComponent.class);

    Object getInput();

    void setInput(Object value);

    boolean hasInput();

    void clear();

    void setError(String isError);

    default void setIsErrorInternal(String errorText, Node thisNode, Label errorDisplay){

        // Hide if not erroring
        boolean isErroring = !errorText.isEmpty();
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
