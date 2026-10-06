package seng202.team1.api;

import seng202.team1.api.photon.PhotonClient;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.database.DAO.StatusReportDAO;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.state.GlobalState;

import java.time.Instant;
import java.util.List;

public class AppClients {

    private GlobalState state;

    /// The handler for geocoding requests
    private PhotonClient photonClient;

    /// The handler for app API requests
    private TrashDBClient taServer;

    public AppClients(GlobalState state) {
        this.state = state;
        photonClient = new PhotonClient();
        taServer = new TrashDBClient();
    }


    public PhotonClient getPhotonClient() {
        return photonClient;
    }

    public TrashDBClient getTaServer() {
        return taServer;
    }
}
