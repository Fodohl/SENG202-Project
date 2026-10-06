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

    public void downloadMissingData() {

        TrashLogDAO trashLogDAO = state.getLocalTables().getTrashTable();
        StatusReportDAO statusReportDAO = state.getLocalTables().getStatusReportsTable();

        taServer.getUserLogs(logs -> {

            List<Instant> localLogs = trashLogDAO.getAll().stream().map(l -> l.createdAt()).toList();

            // Any unkown logs are added to the local db
            logs.forEach(log -> {

                // Log already stored here
                if (localLogs.contains(log.createdAt()))
                    return;

                // Add to the local db (out of sync db id is fine becuase DAO ignores it)
                trashLogDAO.add(log);
            });

        });

        taServer.getUserReports(reports -> {

            List<Instant> localReports = statusReportDAO.getAll().stream().map(r -> r.getSubmittedAt()).toList();

            // Any unkown logs are added to the local db
            reports.forEach(report -> {

                // Log already stored here
                if (localReports.contains(report.getSubmittedAt()))
                    return;

                // Add to the local db (out of sync db id is fine becuase DAO ignores it)
                System.out.println("ADDING!");
                statusReportDAO.add(report);
            });

        });

    }


    public PhotonClient getPhotonClient() {
        return photonClient;
    }

    public TrashDBClient getTaServer() {
        return taServer;
    }
}
