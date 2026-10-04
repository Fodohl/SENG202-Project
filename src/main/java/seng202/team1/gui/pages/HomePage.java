package seng202.team1.gui.pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;

import java.util.Comparator;
import java.util.List;
import seng202.team1.data.status.BinTrollService;

/**
 * Page that displays a overview of the TrashApp
 */
public class HomePage extends Page {

    /// The user's leaderboard position
    private UICard leaderBoard;

    /// The user's trees saved days
    private UICard trees;

    /// Te user's total logged items
    private UICard total;

    /// Users total trolls
    private UICard trolls;

    /**
     * Constructs a new HomePage
     *
     * @param router the router that handles this page
     */
    public HomePage(Router router){

        super(router, Pages.HOME);

    }

    /**
     * Constructs the UI for the home page
     *
     * @return the UI layout for the page
     */
    @Override
    protected Parent renderSelf() {

        // Top cards
        leaderBoard = new UICard("Community Position:", "#4",  "(Top 10%)", "/media/images/dir-continue.svg", true);
        trees = new UICard("Trees Saved:", "1.1",  "Trees", "/media/images/tree.svg", true);
        total = new UICard("Trash Logged:", "184",  "Items", "/media/images/throw.svg", true);
        trolls = new UICard("Trolls Slain:", "0", "Trolls", "/media/images/troll.svg", true);

        HBox topCards = new HBox(20, leaderBoard, trees, trolls, total);
        topCards.setAlignment(Pos.TOP_LEFT);

        // Cards should grow
        for(UICard card : List.of(leaderBoard, trees, trolls, total))
            HBox.setHgrow(card, Priority.ALWAYS);

        StackPane mapView = mapPreview();

        VBox root = new VBox(20, topCards, mapView);
        root.getStyleClass().add("home");
        return root;
    }

    /**
     * Render a preview image of the map
     *
     * @return The rendered map preview component
     */
    private StackPane mapPreview() {

        UIIconButton goToMap = new UIIconButton("/media/images/semi_full.svg", "Find Bin");
        goToMap.getStyleClass().addAll("button", "active");
        goToMap.setOnMouseClicked(e -> router.switchPage(Pages.MAP));


        VBox overlay = new VBox(goToMap);
        overlay.getStyleClass().addAll("overlay");

        UIImage map = new UIImage("/media/images/map.png", 1350, 500);

        StackPane mapView = new StackPane(map, overlay);
        mapView.getStyleClass().add("overlaymap");
        return mapView;
    }

    /**
     * Refresh the stats card to show the latest data
     */
    @Override
    public void updateContents() {

        // Get usage
        List<TrashLog> uses = router.getGlobalState().getTrashLogDAO()
                .getAll().stream()
                .sorted(Comparator.comparing(TrashLog::createdAt))
                .toList();
        List<Bin> bins = uses.stream()
                .map(b -> router.getGlobalState().getBinDAO().getOne(b.binID()))
                .toList();
        total.setText(String.format("%s", bins.size()));

        var session = router.getGlobalState().getAuth().getSession();

        if (session != null && session.userDetails() != null) {
            int trollsSlain = BinTrollService.getTrollsSlain(
                    session.userDetails(),
                    router.getGlobalState()
                            .getStatusReportStore()
                            .getReports()
            );

            trolls.setText(String.valueOf(trollsSlain));
        } else {
            trolls.setText("0");
        }
    }
}
