package seng202.team1.tasks.workers;

import seng202.team1.database.DAO.CommunityCleanlinessDAO;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.state.GlobalState;
import seng202.team1.tasks.Task;
import seng202.team1.tasks.TaskManager;
import seng202.team1.types.trashdb.CommunityCleanliness;

import java.time.Instant;
import java.time.OffsetDateTime;

import static seng202.team1.helpers.DateHelpers.toOffsetTime;

public class CommunityCleanlinessWorker extends Task {

    public CommunityCleanlinessWorker(TaskManager taskManager){
        super(taskManager, "community cleanliness calculations");
    }


    @Override
    public void task(GlobalState state) {
        state.getAppClients().getTaServer().runRecountCommunityTrash();
    }
}
