package seng202.team1.gui;

import javafx.stage.Stage;
import seng202.team1.api.trashdb.TrashDBBackend;
import seng202.team1.api.trashdb.TrashDBClient;
import seng202.team1.gui.pages.Pages;
import seng202.team1.state.GlobalState;
import seng202.team1.types.photon.Feature;
import seng202.team1.types.trashdb.TrashDBCleanlinessResponse;
import seng202.team1.types.trashdb.TrashDBLeaderboardResponse;

public class UIContext {

    private GlobalState state;

    /// The manager of the app's window
    private Router router;

    /// Current user location or null if none is requested
    private Feature requestedLocation;

    /// Current bin to show or null if none is requested
    private Integer requestedShowBinId;

    /// The current leaderboard state
    private TrashDBLeaderboardResponse leaderboard;

    /// The current community cleanliness state
    private TrashDBCleanlinessResponse communityCleanliness;

    public UIContext(GlobalState state, Stage window){
        this.state = state;
        router = new Router(state, window);

    }

    /**
     * Fetch the latest data from the database to show in the UI
     */
    public void fetchData() {

        TrashDBClient taServer = state.getAppClients().getTaServer();

        taServer.getLeaderboard(leaderboardResponse -> {

            leaderboard = leaderboardResponse;
            router.refreshPage(Pages.COMMUNITY);

        });

        taServer.getCommunityCleanliness(communityCleanlinessResponse -> {

            communityCleanliness = communityCleanlinessResponse;
            router.refreshPage(Pages.COMMUNITY);

        });

    }

    /**
     * Switch to the map and try show a request bin
     *
     * @param id The bin to show (must be a int)
     */
    public void goToBin(String id) {

        requestedShowBinId = Integer.valueOf(id);
        router.switchPage(Pages.MAP);
    }

    /**
     * Get the bin show request and clear it
     *
     * @return The bin to show or null if none requested
     */
    public Integer consumeBinShowRequest(){

        Integer binID = requestedShowBinId;
        requestedShowBinId = null;
        return binID;
    }


    /**
     * Set a new map search request for the map to handle
     *
     * @param location The search request
     */
    public void setMapSearchRequest(Feature location) {
        requestedLocation = location;
        router.switchPage(Pages.MAP);
    }

    /**
     * Get the current map search request and clear it
     *
     * @return The search request or null if none
     */
    public Feature consumeMapSearchRequest() {

        // Clear the request
        Feature location = requestedLocation;
        requestedLocation = null;

        return location;
    }

    public Router getRouter() {
        return router;
    }


    public TrashDBLeaderboardResponse getLeaderboard() {
        return leaderboard;
    }

    public TrashDBCleanlinessResponse getCommunityCleanliness() {
        return communityCleanliness;
    }
}
