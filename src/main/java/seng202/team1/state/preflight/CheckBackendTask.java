package seng202.team1.state.preflight;

public class CheckBackendTask extends PreflightTask{

    int delay;

    public CheckBackendTask(PreflightManager manager, int delay) {
        super(manager, String.format("backend availability check %d", delay));
        this.delay = delay;
    }

    @Override
    public void task(PreflightManager manager) {
        try{
            Thread.sleep(delay * 1000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
