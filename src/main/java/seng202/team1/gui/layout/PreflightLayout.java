package seng202.team1.gui.layout;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import seng202.team1.gui.Router;

public class PreflightLayout extends Layout {

    private BorderPane page = null;

    public PreflightLayout(Router router){
        super(router);
    }

    @Override
    protected Parent renderSelf() {

        page = new BorderPane();
        page.getStyleClass().add("preflight");
        return page;

    }


    @Override
    public void swap(Parent rendered) {
        page.setCenter(rendered);
    }
}
