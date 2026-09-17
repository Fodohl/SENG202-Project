package seng202.team1.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.common.StringHelpers;
import seng202.team1.gui.components.UIImage;
import seng202.team1.gui.layout.OverlayLayout;
import seng202.team1.gui.modals.*;
import seng202.team1.gui.layout.MainLayout;
import seng202.team1.gui.layout.LayoutElement;
import seng202.team1.gui.pages.*;
import seng202.team1.state.GlobalState;
import seng202.team1.state.StateReferrer;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Consumer;

import static seng202.team1.gui.GraphicsCommon.WINDOW_HEIGHT;
import static seng202.team1.gui.GraphicsCommon.WINDOW_WIDTH;

/**
 * Manages loading all pages and switching between them. Also manages the main app window and the lifetime of other windows
 */
public class WindowManager extends StateReferrer {

    /// WindowManager specific logger
    private static final Logger log = LogManager.getLogger(UIImage.class);

    /// The window reference this manager is responsible for
    private Stage currentStage;

    /// What page to show on start up
    public static Pages START_PAGE = Pages.DASHBOARD;

    /// The page that is currently shown
    private Pages currentPage;
    private Modals currentModal;

    /// The list of the pages that have been show in order
    private Stack<Pages> history = new Stack<>();

    /// The event handlers triggered when switching pages
    private final List<Consumer<Pages>> pageSwitchCallbacks = new ArrayList<>();

    private final List<Consumer<Modals>> modalSwitchCallbacks = new ArrayList<>();

    /// The pages this window can switch between
    private final Map<Pages, Page> pages = new HashMap<>();

    /// The modals this window can display
    private final Map<Modals, Modal> modals = new HashMap<>();

    /// The list of modals currently being shown in a separate window
    private Map<Long, Stage> windowedModals = new HashMap<>();

    /// The next available ID to assign a window
    private long nextWindowId = 0;

    /// The layout that contains the pages for this window
    private final MainLayout layout = new MainLayout(this);

    /// The lay that contains the overlayed elements for this window
    private final OverlayLayout overlay = new OverlayLayout(this);

    /// The root scene
    private final StackPane root = new StackPane(layout.getRendered(), overlay.getRendered());

    /**
     * Constructs a new WindowManager, loading the pages for this window
     *
     * @param state The reference to global state
     * @param currentStage The main app window
     */
    public WindowManager(GlobalState state, Stage currentStage){
        super(state);

        // Load core ui
        this.currentStage = currentStage;
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Dont load stage if not running with a window
        if(!GlobalState.IS_HEADLESS_MODE)
            currentStage.setScene(scene);

        // Load CSS
        String cssPath = Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);

        // Show dashboard
        loadPages();
        loadModals();
        switchPage(START_PAGE);
        hideOverlay();
    }

    /**
     * Creates & stores a new page for each type
     */
    private void loadPages() {
        loadPage(Pages.DASHBOARD, new DashboardPage(this));
        loadPage(Pages.MAP, new MapPage(this));
        loadPage(Pages.LEADERBOARD, new LeaderboardPage(this));
        loadPage(Pages.TIMELINE, new TimelinePage(this));
        loadPage(Pages.SETTINGS, new SettingsPage(this));
        loadPage(Pages.DATA, new DataPage(this));
    }

    /**
     * Creates & stores a new modal for each type
     */
    private void loadModals() {
        loadModal(Modals.LOG_TRASH, new LogTrashModal(this));
        loadModal(Modals.LOG_ISSUE, new LogIssueModal(this));
        loadModal(Modals.DIRECTIONS, new DirectionsModal(this));
    }

    /**
     * Store a modal given its type
     *
     * Warning: will unsafely fail if modal is already loaded
     *
     * @param type The type of modal
     * @param modal The modal contents
     */
    private void loadModal(Modals type, Modal modal){

        // Should never have two of the same page types
        if(modals.get(type) != null)
            throw new InvalidParameterException("Modal type already exists");

        modals.put(type, modal);
    }

     /**
     * Store a page given its type
     *
     * Warning: will unsafely fail if page is already loaded
     *
     * @param type The type of page
     * @param page The page contents
     */
    private void loadPage(Pages type, Page page){

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
    public void switchPage(Pages pageType) {

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

       // Update pages to show latest data post render
        for (Consumer<Pages> callback : pageSwitchCallbacks)
            callback.accept(pageType);
    }

    /**
     * Get the page currently being displayed
     *
     * @return The page type that is showns
     */
    public Pages getCurrentPage() {
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
    public boolean registerPageChangeCallback(Consumer<Pages> callback){

        // Prevent a callback being triggered twice
        if(pageSwitchCallbacks.contains(callback))
            return false;

        // Store
        pageSwitchCallbacks.add(callback);
        return true;
    }

    /**
     * Register an event handler for a modal change event
     *
     * Warning: an event handler cannot be registered twice
     *
     * @param callback The function to run when a modal change is triggered
     * @return True if the callback was registered successfully
     */
    public boolean registerModalChangeCallback(Consumer<Modals> callback){

        // Prevent a callback being triggered twice
        if(modalSwitchCallbacks.contains(callback))
            return false;

        // Store
        modalSwitchCallbacks.add(callback);
        return true;
    }

    /**
     * Switch to the previously shown page
     */
    public void backPage(){

        // Remove this page from history
        history.pop();

        // Show the previous page
        Pages dest = history.pop();
        switchPage(dest);
    }

    /**
     * Show an element as an overlay
     *
     * @param contents The element to overlay
     */
    public void showOverlay(Parent contents){

        // Remove the currently shown overlay
        hideOverlay();

        // Show the overlay above the core ui
        overlay.setVisible(true);
        overlay.setContents(contents);
    }

    /**
     * Hide the currently shown overlay
     */
    public void hideOverlay(){
        overlay.setVisible(false);
    }

    /**
     * Open a modal as an overlay
     *
     * @see #openModal(Modals, Object)
     */
    public void openModal(Modals modalType){
        openModal(modalType, null);
    }

    /**
     * Open a modal as an overlay
     *
     * @param modalType The modal to show
     * @param data The data to pass to the overlay
     */
    public void openModal(Modals modalType, Object data){

        // Update pages to show latest data pre render
        for (Consumer<Modals> callback : modalSwitchCallbacks)
            callback.accept(modalType);

        // Select the page
        currentModal = modalType;
        Modal modal = modals.get(modalType);

        // Hiding modal
        if(modalType == Modals._CLOSED){
            overlay.setVisible(false);
            return;
        }

        // Page not stored
        if(modal == null){
            log.warn("Modal not loaded for type: {}", modalType.name());
            return;
        }

        // Show the page
        modal.recvData(data);
        showOverlay(modal.getRendered());
    }

    /**
     * Close the shown modal
     */
    public void closeModal(){

        // Run close handlers
        //@TODO

        // Special modal that represents closed state, allows for modals to update self when closed via a change callback
        openModal(Modals._CLOSED);
    }

    /**
     * Open a instance of a modal into a popup window
     *
     * @see #spawnModal(Modals, Object)
     */
    public long spawnModal(Modals modalType){
        return spawnModal(modalType, null);
    }

    /**
     * Open a instance of a modal into a popup window
     *
     * @param modalType The modal to show
     * @param data The data to pass to the overlay
     * @return The ID of the opened modal
     */
    public long spawnModal(Modals modalType, Object data){

        // Select the page
        currentModal = modalType;
        Modal modal = modals.get(modalType);

        // Page not stored
        if(modal == null){
            log.warn("Modal not loaded for type: {}", modalType.name());
            return -1;
        }

        // Setup a new modal instance for this window
        modal = modal.clone();
        modal.recvData(data);

        // Contain the modal in a separate root so it can be in multiple windows
        StackPane modalRoot = new StackPane(modal.getRendered());
        modalRoot.getStyleClass().add("modal-window");

        // Create the window
        Stage stage = new Stage();
        stage.setTitle(StringHelpers.enumNameToTitle(modalType.name()));

        // Put the modal in the window
        Scene scene = new Scene(modalRoot);
        stage.setScene(scene);

        // Style the window @todo func
        String cssPath = Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);

        // Spawn the window to the right of the map
        stage.setX(currentStage.getX() + GraphicsCommon.precentWindowX(0.75));
        stage.show();

        // Assign the window an ID for tracking
        long windowID = nextWindowId++;
        modal.setWindowed(windowID);

        // Store
        windowedModals.put(windowID, stage);
        return windowID;
    }

    /**
     * Close the window for a popup modal
     *
     * @param windowID The ID of the popup modal
     */
    public void killModal(long windowID){

        // Get the modal
        Stage window = windowedModals.get(windowID);
        if(window == null)
            return;

        // Kill
        window.close();
        windowedModals.remove(windowID);
    }

    /**
     * Force a soft-rerender of a page
     *
     * @param pageType The page to update
     */
    public void refreshPage(Pages pageType) {

        Page page = pages.get(pageType);
        if(page == null)
            return;

        page.updateContents();
    }

    /**
     * Get the current window
     *
     * @return The current window
     */
    public Stage getStage() {
        return currentStage;
    }
}