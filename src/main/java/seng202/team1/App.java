package seng202.team1;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.MainWindow;
import seng202.team0.data_managers.*;

/**
 * Default entry point class
 * @author seng202 teaching team
 */
public class App {
    private static final Logger log = LogManager.getLogger(App.class);

    /**
     * Entry point which runs the javaFX application
     * Also shows off some different logging levels
     * @param args program arguments from command line
     */
    public static void main(String[] args) {
        //tests
        // DataLoader dataLoader = new DataLoader();
        // dataLoader.readGeoJSON();
        // System.out.println("Number of bins loaded: " + dataLoader.binObjects.size());
        // System.out.println("First bin coordinates: " + dataLoader.binObjects.get(0).getBinLatitude() + ", " + dataLoader.binObjects.get(0).getBinLongitude());
        // System.out.println(dataLoader.binObjects.get(0));

        MainWindow.main(args);

    }
}
