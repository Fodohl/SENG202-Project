package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UICard;

import java.util.Comparator;
import java.util.List;

/**
 * Page that displays a overview of the TrashApp
 */
public class DashboardPage extends Page {

    private UICard leaderBoard;
    private UICard trees;
    private UICard dataset;
    private UICard lastLog;
    private UICard totalLogged;
    private UICard trashLogged;
    private UICard recycLogged;

    /**
     * Constructs a new DashboardPage
     *
     * @param windowManager the window this page is on
     */
    public DashboardPage(WindowManager windowManager){

        super(windowManager, Pages.DASHBOARD);

    }

    /**
     * Constructs the UI for the dashboard page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        //@todo add graph and make look nicer

        // Top cards
        leaderBoard = new UICard("Leaderboard Position:", "#4",  "(Top 10%)", "/images/dir-continue.svg", true);
        trees = new UICard("Trees Saved:", "1.1",  "Trees", "/images/tree.svg", true);
        dataset = new UICard("Dataset:", "184",  "Bins Loaded", "/images/data.svg", true);

        HBox topCards = new HBox(20, leaderBoard, trees, dataset);
        topCards.setAlignment(Pos.CENTER);

        // Centre main card
        lastLog = new UICard("Recent Activity:", "Logged entry at {type} bin in {place}",  "{date}", "/images/bin.png");
        lastLog.resizeIcon(150, 150);
        lastLog.getStyleClass().add("active-grad");

        // Bottom cards
        totalLogged = new UICard("Total Items Logged:", "450",  "", "/images/throw.svg");
        recycLogged = new UICard("Total Items Recycled:", "300",  "", "/images/recycle.svg");
        trashLogged = new UICard("Total Items Rubbish:", "150",  "", "/images/bin.svg");

        HBox botCards = new HBox(20, totalLogged, recycLogged, trashLogged);
        botCards.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, topCards, lastLog, botCards);
        root.getStyleClass().add("dashboard");
        return root;
    }

    //@todo auto attach bin to log class

    @Override
    protected void updateContents() {

        dataset.setText(String.format("%s", windowManager.getGlobalState().getDataLoader().getBinObjects().size()));

        // Get usage
        List<TrashLog> uses = windowManager.getGlobalState().getTrashLogStore()
                                           .getAll().stream()
                                           .sorted(Comparator.comparing(TrashLog::createdAt))
                                           .toList();
        List<Bin> bins = uses.stream()
                              .map(b -> windowManager.getGlobalState().getDataLoader().getBinById(b.binLogId()))
                              .toList();

        TrashLog newestLog = uses.isEmpty() ? null : uses.getLast();
        Bin newestBin = bins.isEmpty() ? null : bins.getLast();


        totalLogged.setText(String.format("%d", newestBin == null ? 0 : bins.size()));
        trashLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.GENERAL_WASTE).toList().size()));
        recycLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.RECYCLABLE).toList().size()));

        // Hasn't logged
        if(newestLog == null){
            lastLog.setText("No trash logged yet.");
            lastLog.setSubtitle("Use the map to log some trash");
            return;
        }

        lastLog.setText(String.format("Logged entry at %s bin in %s.", StringHelpers.enumNameToTitle(newestBin.getCategory().name()), newestBin.getAreaName()));
        lastLog.setSubtitle(StringHelpers.toLocaleString(newestLog.disposedAt()));

    }
}
