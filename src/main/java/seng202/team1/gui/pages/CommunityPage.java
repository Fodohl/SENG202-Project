package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.api.tasever.LeaderBoardEntry;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;

import java.util.Comparator;

/**
 * Page that displays a ranking of the TrashApps users
 */
public class CommunityPage extends Page {

    private StringTableBuilder<LeaderBoardEntry> tableData = null;

    private UITable table;

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
        UIGraphLine communiy = new UIGraphLine();

        // Combine
        VBox root = new VBox(50, communiy, pageTitle, table);
        root.getStyleClass().add("communiy");
        return root;

    }

    @Override
    public void updateContents() {

        if(tableData == null)
            return;

        // Show the latest leaderboard info
        tableData.updateData(router.getGlobalState().getCommunity().entries());
        table.buildTableContent();
    }
}
