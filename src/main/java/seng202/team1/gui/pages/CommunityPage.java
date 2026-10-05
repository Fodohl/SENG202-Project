package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.types.trashdb.LeaderBoardEntry;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;
import seng202.team1.types.trashdb.TrashDBCleanlinessResponse;

import java.util.Comparator;

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
        String thisUser = "Mike M";
        tableData = new StringTableBuilder<LeaderBoardEntry>(router.getGlobalState().getCommunity().entries())
                .addColumn("position", "Position")
                .addColumn("name", "Name", item -> {

                    // Indicate which row the user belongs to
                    String name = (String)item;
                    return thisUser.equals(name) ? name + " (You)" : name;
                })
                .addColumn("items_logged", "Trash")
                .addColumn("trees", "Trees Saved")
                .sortBy(Comparator.comparing(item -> item.position()));

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
        if(root == null)
            return;

        TrashDBCleanlinessResponse graphData = getState().getCommunityClealiness();
        if(graphData != null){
            communiy.setData(graphData.counts());
            root.getChildren().set(0, communiy);
        } else{
            root.getChildren().set(0, new UISpinner(50));
        }

        if(tableData == null)
            return;

        // Show the latest leaderboard info
        tableData.updateData(router.getGlobalState().getCommunity().entries());
        table.buildTableContent();
    }
}
