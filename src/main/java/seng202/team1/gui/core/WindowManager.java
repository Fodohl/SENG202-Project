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

/**
 * Manages loading all pages and switching between them
 */
public class WindowManager extends StateReferrer {

    /// WindowManager specific logger
    private static final Logger log = LogManager.getLogger(UIImage.class);

    /// The window reference this manager is responsible for
    private Stage currentStage;

    /// What page to show on start up
    public static PageType START_PAGE = PageType.MAP;

    /// The page that is currently shown
    private PageType currentPage;

    /// The list of the pages that have been show in order
    private Stack<PageType> history = new Stack<>();

    /// The event handlers triggered when switching pages
    private final List<Consumer<PageType>> pageSwitchCallbacks = new ArrayList<>();

    /// The pages this window can switch between
    private final Map<PageType, Page> pages = new HashMap<>();

    /// The layout that contains the pages for this window
    private final CoreUI layout = new CoreUI(this);

    /**
     * Constructs a new WindowManager, loading the pages for this window
     *
     * @param state The reference to global state
     * @param currentStage The window this manager is responsible for
     */
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
        switchPage(START_PAGE);
    }

    /**
     * Creates & stores a new page for each type
     */
    private void loadPages() {
        loadPage(PageType.DASHBOARD, new DashboardPage(this));
        loadPage(PageType.MAP, new MapPage(this));
        loadPage(PageType.LEADERBOARD, new LeaderboardPage(this));
        loadPage(PageType.TIMELINE, new TimelinePage(this));
        loadPage(PageType.SETTINGS, new SettingsPage(this));
        loadPage(PageType.DATA, new DataPage(this));
    }

    /**
     * Store a page given its type
     *
     * Warning: will unsafely fail if page is already loaded
     *
     * @param type The type of page
     * @param page The page contents
     */
    private void loadPage(PageType type, Page page){

        // Should never have two of the same page types
        if(pages.get(type) != null)
            throw new InvalidParameterException("Page type already exists");

        pages.put(type, page);
    }

    /**
     * Change the active page to another desired one
     *
     * Note: idempotent
     * Note: will not switch if page not loaded, warning will be logged
     *
     * @param pageType The page to switch to
     */
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

    /**
     * Get the page currently being displayed
     *
     * @return The page type that is showns
     */
    public PageType getCurrentPage() {
        return currentPage;
    }

    /**
     * Register an event handler for a page change event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a page change is triggered
     * @return True if the callback was registered successfully
     */
    public boolean registerPageChangeCallback(Consumer<PageType> callback){

        // Prevent a callback being triggered twice
        if(pageSwitchCallbacks.contains(callback))
            return false;

        // Store
        pageSwitchCallbacks.add(callback);
        return false;
    }

    /**
     * Switch to the previously shown page
     */
    public void backPage(){

        // Remove this page from history
        history.pop();

        // Show the previous page
        PageType dest = history.pop();
        switchPage(dest);

    }
}
