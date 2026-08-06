package seng202.team1.state;

/**
 * A object that stores a reference to the global app state
 */
public class StateReferrer {

    /// The active global state for the app
    protected GlobalState state;

    /**
     * Constructs a new StateReferrer
     *
     * @param state The app state reference
     */
    public StateReferrer(GlobalState state){
        this.state = state;
    }

    /**
     * Updates the state to point to a new global state
     * Called when the GlobalState reference changes, not the object's state itself
     *
     * @param state The new state reference for the app
     */
    public void stateChanged(GlobalState state){
        this.state = state;
    }

    /**
     * Get the current global state of the app
     *
     * @return The app state
     */
    public GlobalState getGlobalState(){
        return state;
    }

}
