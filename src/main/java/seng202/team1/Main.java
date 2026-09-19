package seng202.team1;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.sqlite.exceptions.InstanceAlreadyExistsException;
import seng202.team1.gui.components.UIImage;
import seng202.team1.state.GlobalState;

import java.awt.*;
import static javafx.application.Application.launch;

// import net.maxtyson.jfxhr.HotReloader;
// import net.maxtyson.jfxhr.ReloaderConfig;

/**
 * Setups the JavaFX application and launches the app
 */
public class Main extends Application {

    /**
     * Sets the app icon to the TrashApp logo
     *
     * @param stage The window to set the icon for
     */
    public void setAppIcon(Stage stage) {

        // Set icon
        UIImage appLogo = new UIImage("/media/images/icon.png", 512, 512);
        Image logoIcon = appLogo.toRawImage();
        stage.getIcons().add(logoIcon);

        // Check if the taskbar needs to be set aswell
        if (!java.awt.Taskbar.isTaskbarSupported())
            return;

        // Task bar doesnt support icons
        Taskbar taskbar = java.awt.Taskbar.getTaskbar();
        if (!taskbar.isSupported(java.awt.Taskbar.Feature.ICON_IMAGE))
            return;

        // Set the icon
        taskbar.setIconImage(SwingFXUtils.fromFXImage(logoIcon, null));
    }

    /**
     * The JavaFX entry point to the app. Will set up the window and show the main menu
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     */
    @Override
    public void start(Stage stage) throws InstanceAlreadyExistsException {

        // Dev tools
        CSSFX.start();
//        HotReloader.attach(config, stage);

        // Set up the window
        setAppIcon(stage);
        stage.setTitle("TrashApp v1");

        // Setup app state
        GlobalState state = new GlobalState(stage, false);
        stage.show();
    }


    /**
     * Entry point to the application. Launches the JavaFX runtime
     *
     * @param args Command line arguments, unused
     */
    public static void main(String[] args) {
        System.setProperty("javafx.verbose", "true");
        launch();
    }

}
