package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.types.trashdb.LeaderBoardEntry;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.types.trashdb.TrashDBCleanlinessResponse;
import seng202.team1.types.trashdb.TrashDBLeaderboardResponse;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.user.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static seng202.team1.data.treedays.TreeDaysCalculator.getTreeDays;
import static seng202.team1.helpers.LeaderBoardHelpers.toRankedOrder;

/**
 * Page that displays a ranking of the TrashApps users
 */
public class CommunityPage extends Page {

    /// The leaderboard data
    private StringTableBuilder<LeaderBoardEntry> tableData = null;

    /// The table that displays the leaderboard
    private UITable table;

    /// The toplevel element of this page
    private VBox root;

    /// The line graph that display the community cleanlienss
    private UIGraphLine communiy;

    /**
     * Constructs a new CommunityPage
     *
     * @param router the window this page is on
     */
    public CommunityPage(Router router) {

        super(router, Pages.COMMUNITY);

    }

    /**
     * Constructs the UI for the community page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        // Title
        UITitle pageTitle = new UITitle("Leaderboard");
        pageTitle.setAlignment(Pos.CENTER);

        // Data
        tableData = new StringTableBuilder<LeaderBoardEntry>(new ArrayList<>())
                .addColumn("place", "Position")
                .addColumn("userName", "Name", item -> {

                    // Must use the users' name here as it may have changed in their settings, but the leaderboard wont
                    // have refreshed that yet in its entry
                    User user = getState().getAuth().getSession().userDetails();

                    // Indicate which row the user belongs to
                    return user.id().equals(item.userId()) ? user.name() + " (You)" : item.userName();
                })
                .addColumn("logCount", "Trash")
                .addColumn("treeDays", "Trees Days Saved")
                .sortBy(Comparator.comparing(item -> item.place()));

        table = new UITable(tableData, true, row -> row.get("Name").contains("(You)"));

        // Milestone
        communiy = new UIGraphLine();

        // Combine
        root = new VBox(50, communiy, pageTitle, table);
        root.getStyleClass().add("communiy");
        return root;

    }

    /**
     * Refresh the leaderboard with the latest info from the global state
     */
    @Override
    public void updateContents() {

        // Cant update on first render
        if (root == null)
            return;

        TrashDBCleanlinessResponse graphData = getState().getCommunityClealiness();
        TrashDBLeaderboardResponse leaderboardData = getState().getLeaderboard();

        if (graphData != null) {
            communiy.setData(graphData.counts());
            root.getChildren().set(0, communiy);
        } else {
            root.getChildren().set(0, new UISpinner(50));
        }

        // Nothing to display yet
        if (leaderboardData == null)
            root.getChildren().set(2, new UISpinner(50));

        // Calculate the users latest stat
        User user = getState().getAuth().getSession().userDetails();
        List<TrashLog> logs = getState().getTrashLogDAO().getAll();
        int trashlogged = logs.size();
        double treeDays = getTreeDays(logs);


        // Make sure has latest tree score
        List<LeaderBoardEntry> updated  = leaderboardData.entries().stream().map(entry -> {

            // Assume what the database has for others is latest
            if(entry.userId() != user.id())
                return entry;

            return new LeaderBoardEntry(-1, user.id(), user.name(), trashlogged, treeDays, Instant.now());

        }).toList();

        // Display table
        tableData.updateData(toRankedOrder(updated));
        table.buildTableContent();
        root.getChildren().set(2, table);
    }
}
