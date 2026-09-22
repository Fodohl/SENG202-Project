package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.GraphicsCommon;

<<<<<<< HEAD

public abstract interface UIInputComponent {
=======
/**
 * Provides common interfacing with input components
 *
 * Note: state implementation and boilerplate is messy here due to the limitations of Java on multiple subclasses
 */
public interface UIInputComponent {
>>>>>>> main

    /// UIInputComponent specific logger
    static final Logger log = LogManager.getLogger(UIInputComponent.class);

<<<<<<< HEAD
    String errorText = "";

=======
>>>>>>> main
    Object getInput();

    void setInput(Object value);

    boolean hasInput();

    void clear();

    void setError(String isError);

    boolean isErroring();

<<<<<<< HEAD
=======
   /**
     * Check if the current input has resulted in an error
    *
     * @return True if there is an error set
     */
>>>>>>> main
    default boolean isErroringInternal(Label errorDisplay){

        if(errorDisplay == null)
            return false;

        return !errorDisplay.getText().isEmpty();
    }

<<<<<<< HEAD
=======
    /**
     * Update the internal error state of the input component
     *
     * @param errorText The error text, or empty if no errors
     * @param thisNode The base input element
     * @param errorDisplay The error text display
     */
>>>>>>> main
     default void setIsErrorInternal(String errorText, Node thisNode, Label errorDisplay){

        // Clearing error
        if(errorText.isEmpty()){
            errorDisplay.setText(errorText);
<<<<<<< HEAD
=======
            GraphicsCommon.toggleClass(false, errorDisplay, "error");
            GraphicsCommon.toggleClass(false, thisNode, "error");
>>>>>>> main
            return;
        }

        // Hide if not erroring
<<<<<<< HEAD
=======
        errorDisplay.setText(String.format("Error: %s", errorText));
>>>>>>> main
        boolean isErroring = isErroring();
        errorDisplay.setVisible(isErroring);

        // Log
<<<<<<< HEAD
        errorDisplay.setText(String.format("Error: %s", errorText));
=======
>>>>>>> main
        if(isErroring)
            log.error(errorText);

        // Style
        GraphicsCommon.toggleClass(isErroring, errorDisplay, "error");
        GraphicsCommon.toggleClass(isErroring, thisNode, "error");
    }

}
