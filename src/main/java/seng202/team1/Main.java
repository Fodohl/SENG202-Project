package seng202.team1;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.team1.database.exceptions.InstanceAlreadyExistsException;
import seng202.team1.debug.JFRMarker;
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

        // Check if the taskbar needs to be set as well
        if (!java.awt.Taskbar.isTaskbarSupported())
            return;

        // Task bar doesnt support icons
        Taskbar taskbar = java.awt.Taskbar.getTaskbar();
        if (!taskbar.isSupported(java.awt.Taskbar.Feature.ICON_IMAGE))
            return;

        // Set the icon
        taskbar.setIconImage(SwingFXUtils.fromFXImage(logoIcon, null));
    }

    void deferLoadAppIcon(Stage stage){
         try{
            Thread.sleep(800);
        } catch (InterruptedException e) {
            //nvm
        }

        // Begin loading
        Platform.runLater(() -> setAppIcon(stage));
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

        // Setup app state
        GlobalState state = new GlobalState(stage);
        stage.show();

         // Set up the window
        state.getAppTasks().getBackgroundTasks().runTask("Setup App Icon", e -> deferLoadAppIcon(stage));
        stage.setTitle("TrashApp v1");

        JFRMarker guiReady = new JFRMarker("GUI Shown");
        guiReady.commit();
        TrashApp.startUpTime.commit();

        long end = System.nanoTime();
        System.out.printf("Took %.1f ms%n", (end - TrashApp.start) / 1_000_000.0);
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
