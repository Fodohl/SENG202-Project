package seng202.team1.gui.components;


import javafx.scene.control.Button;

/**
 * A UI Component to handle rendering buttons
 */
public class UIButton extends Button {

    private String prompt;

    /**
     * Construct a new primary button
     *
     * @param prompt The prompt to put on the button
     */
    public UIButton(String prompt){
        this(prompt, false);
    }

     /**
     * Construct a new  button
     *
     * @param prompt The prompt to put on the button
     * @param secondary Whether the button is a primary input or secondary input
     */
    public UIButton(String prompt, boolean secondary){
        super(prompt);
        this.prompt = prompt;

        getStyleClass().add("button");
        if(secondary)
            getStyleClass().add("secondary");
    }

    /**
     * Enable the loading state of the button, disabling it
     *
     * @param isLoading Whether the button is in loading state or not
     */
    public void setSpinner(boolean isLoading) {

        if(isLoading){
            setText("...");
            setDisable(true);
        } else{
            setText(prompt);
            setDisable(false);
        }

    }
}
