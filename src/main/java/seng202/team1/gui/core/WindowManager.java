package seng202.team1.gui.core;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.pages.*;
import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Consumer;

import static seng202.team1.gui.GraphicsCommon.WINDOW_HEIGHT;
import static seng202.team1.gui.GraphicsCommon.WINDOW_WIDTH;

public class WindowManager extends StateReferrer {

    private static final Logger log = LogManager.getLogger(UIImage.class);

    private Stage currentStage;
    private PageType currentPage;
    private Stack<PageType> history = new Stack<>();

    private final List<Consumer<PageType>> pageSwitchCallbacks = new ArrayList<>();
    private final Map<PageType, LayoutElement> pages = new HashMap<>();

    private final CoreUI layout = new CoreUI(this);

    public WindowManager(GlobalState state, Stage currentStage){
        super(state);

        // Load core ui
        this.currentStage = currentStage;
        Scene scene = new Scene(layout.getRendered(), WINDOW_WIDTH, WINDOW_HEIGHT);

        // Dont load stage if not running with a window
        if(!GlobalState.IS_HEADLESS_MODE)
            currentStage.setScene(scene);

        // Load CSS
        String cssPath = Objects.requireNonNull(getClass().getResource("/fxcss/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);

        // Show dashboard
        loadPages();
        switchPage(PageType.DATA);
    }

    private void loadPages() {
        loadPage(PageType.DASHBOARD, new DashboardPage(this));
        loadPage(PageType.LEADERBOARD, new LeaderboardPage(this));
        loadPage(PageType.TIMELINE, new TimelinePage(this));
        loadPage(PageType.SETTINGS, new SettingsPage(this));
        loadPage(PageType.DATA, new DataPage(this));

        // No need to preload if not being shown
        if(GlobalState.IS_HEADLESS_MODE)
            return;

        // Preload the map so that it doesn't lag
        LayoutElement map = new MapPage(this);
        map.rerender();
        loadPage(PageType.MAP, map);
    }

    private void loadPage(PageType type, LayoutElement page){

        // Should never have two of the same page types
        if(pages.get(type) != null)
            throw new InvalidParameterException("Page type already exists");

        pages.put(type, page);
    }

    public void switchPage(PageType pageType) {

        // Update pages to show latest data pre render
        for (Consumer<PageType> callback : pageSwitchCallbacks)
            callback.accept(pageType);

        // Select the page
        currentPage = pageType;
        LayoutElement page = pages.get(pageType);

        // Page not stored
        if(page == null){
            log.warn("Page not loaded for type: {}", pageType.name());
            return;
        }

        // Show the page
        history.add(currentPage);
        layout.swap(page.getRendered());
    }

    public PageType getCurrentPage() {
        return currentPage;
    }

    public boolean registerPageChangeCallback(Consumer<PageType> callback){

        // Prevent a callback being triggered twice
        if(pageSwitchCallbacks.contains(callback))
            return false;

        // Store
        pageSwitchCallbacks.add(callback);
        return false;
    }
}
