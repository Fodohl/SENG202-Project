package seng202.team1;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.debug.JFRMarker;

/**
 * Default entry point class
 * @author Max Tyson
 */
public class TrashApp {

     /// TrashApp specific logger
     private static final Logger log = LogManager.getLogger(TrashApp.class);

     public static final Gson GSON = new Gson();

    public static JFRMarker startUpTime;

    /**
     * Entry point to the application. Starts the main JavaFX class
     *
     * @param args Commandline arguments to pass to the main class
     */
    public static void main(String[] args) {
        log.info("Starting JavaFX...");
        JFRMarker startApp = new JFRMarker("Start App");
        startApp.commit();
        startUpTime = new JFRMarker("Start Up Time");

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Main.main(args);
    }

}
