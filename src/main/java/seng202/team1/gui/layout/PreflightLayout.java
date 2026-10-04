package seng202.team1.gui.layout;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;

/**
 * Composite UI component that contains elements consistent across the PREFLIGHT phase of the app
 */
public class PreflightLayout extends Layout {

    private BorderPane page = null;

    public PreflightLayout(Router router){
        super(router);
    }

    /**
     * Combine UI elements to build the layout
     *
     * @return A new root node containing the rendered layout
     */
    @Override
    protected Parent renderSelf() {

        page = new BorderPane();
        page.getStyleClass().add("preflight");
        return page;

    }

     /**
     * Update the contents section to show a new pages
     *
     * @param rendered The rendered page to show
     */
    @Override
    public void swap(Parent rendered) {
        page.setCenter(rendered);
    }
}
