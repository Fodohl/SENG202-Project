package seng202.team1.state;

public class StateReferrer {


    protected GlobalState state;

    public StateReferrer(GlobalState state){
        this.state = state;
    }

    /**
     * Updates the state to point to a new global state
     * Called when the GlobalState reference changes, not the object's state itself
     *
     * @param state
     */
    public void stateChanged(GlobalState state){
        this.state = state;
    }

    public GlobalState getGlobalState(){
        return state;
    }

}
