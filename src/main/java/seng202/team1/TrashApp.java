package seng202.team1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default entry point class
 * @author Max Tyson
 */
public class TrashApp {

     /// TrashApp specific logger
     private static final Logger log = LogManager.getLogger(TrashApp.class);

    /**
     * Entry point to the application. Starts the main JavaFX class
     *
     * @param args Commandline arguments to pass to the main class
     */
    public static void main(String[] args) {
        log.info("Starting JavaFX...");
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Main.main(args);
    }

}
