package seng202.team1.state.preflight;

public class CheckBackendTask extends PreflightTask{

    public CheckBackendTask(PreflightManager manager) {
        super(manager, "backend availability check");
    }

    @Override
    public void task(PreflightManager manager) {
    }
}
