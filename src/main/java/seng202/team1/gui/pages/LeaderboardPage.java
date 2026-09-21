package seng202.team1.gui.pages;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng202.team1.api.tasever.LeaderBoardEntry;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.components.UISlider;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITitle;

import java.util.Comparator;

/**
 * Page that displays a ranking of the TrashApps users
 */
public class LeaderboardPage extends Page {

    private StringTableBuilder<LeaderBoardEntry> tableData = null;

    private UITable table;

    private HBox milstone;

    /**
     * Constructs a new LeaderboardPage
     *
     * @param windowManager the window this page is on
     */
    public LeaderboardPage(WindowManager windowManager) {

        super(windowManager, Pages.LEADERBOARD);

    }

    public HBox buildNextMileStone(){

        UIImage icon = new UIImage("/media/images/medal-gold.svg", 100, 100);

        Label nextMilestone = new Label("Next Milestone: 1.5 Trees Saved!");
        UISlider progress = new UISlider(true);
        progress.setInput(80.0);

        VBox nextContainer = new VBox(nextMilestone, progress);
        HBox container =  new HBox(100, icon, nextContainer);

        nextContainer.getStyleClass().add("milestone-container");
        nextContainer.setMaxWidth(Double.MAX_VALUE);


        return container;
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

        // Data
        String thisUser = "Mike M";
        tableData = new StringTableBuilder<LeaderBoardEntry>(windowManager.getGlobalState().getLeaderboard().entries())
                .addColumn("position", "Position")
                .addColumn("name", "Name", item -> {

                    // Indicate which row the user belongs to
                    String name = (String)item;
                    return thisUser.equals(name) ? name + " (You)" : name;
                })
                .addColumn("items_logged", "Items Logged")
                .addColumn("trees", "Trees Saved")
                .sortBy(Comparator.comparing(item -> item.position()));

        table = new UITable(tableData, true, row -> row.get("Name").contains("(You)"));

        // Milestone
        milstone = new HBox(buildNextMileStone());
        milstone.getStyleClass().add("milestone");
        milstone.setAlignment(Pos.CENTER);

        // Combine
        VBox root = new VBox(50, pageTitle, milstone, table);
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

        HBox newInner = buildNextMileStone();
        milstone.getChildren().set(0, newInner);
    }
}
