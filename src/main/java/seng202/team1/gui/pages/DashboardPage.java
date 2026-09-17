package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.UICard;
import seng202.team1.gui.components.UIGraphLine;

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
    private UICard organicLogged;
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

        // Top cards
        leaderBoard = new UICard("Leaderboard Position:", "#4",  "(Top 10%)", "/media/images/dir-continue.svg", true);
        trees = new UICard("Trees Saved:", "1.1",  "Trees", "/media/images/tree.svg", true);
        dataset = new UICard("Trash Logged:", "184",  "Items", "/media/images/throw.svg", true);

        HBox topCards = new HBox(20, leaderBoard, trees, dataset);
        topCards.setAlignment(Pos.TOP_LEFT);

        // Graph
        UIGraphLine communiy = new UIGraphLine();

        // Centre main card
        lastLog = new UICard("Recent Activity:", "Logged entry at {type} bin in {place}",  "{date}", "/media/images/bin.png");
        lastLog.resizeIcon(150, 150);
        lastLog.getStyleClass().add("active-grad");

        // Bottom cards
        trashLogged = new UICard("Total Rubbish Logged:", "150",  "", "/media/images/bin.svg");
        recycLogged = new UICard("Total Recycling Logged:", "300",  "", "/media/images/recycle.svg");
        organicLogged = new UICard("Total Organics Logged:", "450",  "", "/media/images/organic.svg");

        HBox botCards = new HBox(20, trashLogged, recycLogged, organicLogged);
        botCards.setAlignment(Pos.TOP_LEFT);

        // Cards should grow
        for(UICard card : List.of(leaderBoard, trees, dataset, organicLogged, recycLogged, trashLogged))
            HBox.setHgrow(card, Priority.ALWAYS);

        VBox root = new VBox(20, topCards, communiy, lastLog, botCards);
        root.getStyleClass().add("dashboard");
        return root;
    }

    //@todo auto attach bin to log class

    @Override
    public void updateContents() {


        // Get usage
        List<TrashLog> uses = windowManager.getGlobalState().getTrashLogStore()
                                           .getAll().stream()
                                           .sorted(Comparator.comparing(TrashLog::createdAt))
                                           .toList();
        List<Bin> bins = uses.stream()
                              .map(b -> windowManager.getGlobalState().getBinLoader().getBinById(b.binLogId()))
                              .toList();

        TrashLog newestLog = uses.isEmpty() ? null : uses.getLast();
        Bin newestBin = bins.isEmpty() ? null : bins.getLast();

        dataset.setText(String.format("%s", bins.size()));

        trashLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.GENERAL_WASTE).toList().size()));
        recycLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.RECYCLABLE).toList().size()));
        organicLogged.setText(String.format("%d", newestBin == null ? 0 : bins.stream().filter(b -> b.getCategory() == BinCategory.ORGANIC).toList().size()));

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
