package seng202.team1.gui.components;


import javafx.scene.control.Button;

<<<<<<< HEAD
public class UIButton extends Button {

=======
/**
 * A UI Component to handle rendering buttons
 */
public class UIButton extends Button {

    /**
     * Construct a new primary button
     *
     * @param prompt The prompt to put on the button
     */
>>>>>>> main
    public UIButton(String prompt){
        this(prompt, false);
    }

<<<<<<< HEAD
=======
     /**
     * Construct a new  button
     *
     * @param prompt The prompt to put on the button
     * @param secondary Whether the button is a primary input or secondary input
     */
>>>>>>> main
    public UIButton(String prompt, boolean secondary){
        super(prompt);

        getStyleClass().add("button");
        if(secondary)
            getStyleClass().add("secondary");
    }
}
