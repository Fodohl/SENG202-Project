package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng202.team1.api.trashdb.LeaderBoardEntry;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;

import java.util.Comparator;

/**
 * Page that displays a ranking of the TrashApps users
 */
public class LeaderboardPage extends Page {

    private StringTableBuilder<LeaderBoardEntry> tableData = null;

    private UITable table;

    /**
     * Constructs a new LeaderboardPage
     *
     * @param windowManager the window this page is on
     */
    public LeaderboardPage(WindowManager windowManager) {

        super(windowManager, Pages.LEADERBOARD);

    }

    /**
     * Constructs the UI for the leaderboard page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {


        // Title
        UITitle pageTitle = new UITitle("Leaderboard");
        pageTitle.setAlignment(Pos.CENTER);

        String thisUser = "Mike M";

        // Data
        tableData = new StringTableBuilder<LeaderBoardEntry>(windowManager.getGlobalState().getLeaderboard().entries())
                .addColumn("position", "Position")
                .addColumn("name", "Name", item -> {

                    String name = (String)item;
                    return thisUser.equals(name) ? name + " (You)" : name;
                })
                .addColumn("items_logged", "Items Logged")
                .addColumn("trees", "Trees Saved")
                .sortBy(Comparator.comparing(item -> item.position()));

        table = new UITable(tableData, true, row -> row.get("Name").contains("(You)"));


        // Combine
        VBox root = new VBox(20, pageTitle, table);
        root.setAlignment(Pos.CENTER);
        return root;

    }

    @Override
    public void updateContents() {

        if(tableData == null)
            return;

        // Show the latest leaderboard info
        tableData.updateData(windowManager.getGlobalState().getLeaderboard().entries());
        table.buildTableContent();

    }
}
