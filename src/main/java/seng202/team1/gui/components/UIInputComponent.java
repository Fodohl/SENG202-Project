package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.GraphicsCommon;

/**
 * Provides common interfacing with input components
 *
 * Note: state implementation and boilerplate is messy here due to the limitations of Java on multiple subclasses
 */
public interface UIInputComponent {

    /// UIInputComponent specific logger
    static final Logger log = LogManager.getLogger(UIInputComponent.class);

    Object getInput();

    void setInput(Object value);

    boolean hasInput();

    void clear();

    void setError(String isError);

    boolean isErroring();

   /**
     * Check if the current input has resulted in an error
    *
     * @return True if there is an error set
     */
    default boolean isErroringInternal(Label errorDisplay){

        if(errorDisplay == null)
            return false;

        return !errorDisplay.getText().isEmpty();
    }

    /**
     * Update the internal error state of the input component
     *
     * @param errorText The error text, or empty if no errors
     * @param thisNode The base input element
     * @param errorDisplay The error text display
     */
     default void setIsErrorInternal(String errorText, Node thisNode, Label errorDisplay){

        // Clearing error
        if(errorText.isEmpty()){
            errorDisplay.setText(errorText);
            GraphicsCommon.toggleClass(false, errorDisplay, "error");
            GraphicsCommon.toggleClass(false, thisNode, "error");
            return;
        }

        // Hide if not erroring
        errorDisplay.setText(String.format("Error: %s", errorText));
        boolean isErroring = isErroring();
        errorDisplay.setVisible(isErroring);

        // Log
        if(isErroring)
            log.error(errorText);

        // Style
        GraphicsCommon.toggleClass(isErroring, errorDisplay, "error");
        GraphicsCommon.toggleClass(isErroring, thisNode, "error");
    }

}
