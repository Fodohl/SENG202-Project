package seng202.team1.gui;

import javafx.scene.Node;
import seng202.team1.Main;

/**
 * Utility class that provides helpers for common graphics tasks
 */
public class GraphicsCommon {

    /// How wide the window is in pixels
    public static final int WINDOW_WIDTH = 1700;

    /// How tall the window is in pixels
    public static final int WINDOW_HEIGHT = 850;

    /**
     * Get a width as a percentage of the window
     *
     * @param percent Ratio of across pixels, between 0 and 1
     * @return The desired width in pixels, 0 if invalid ratio
     */
    public static int precentWindowX(double percent){

        if(percent < 0 || percent > 1)
            return 0;

        return (int) Math.floor( percent * WINDOW_WIDTH);
    }

     /**
      * Get a height as a percentage of the window
      *
      * @param percent Ratio of length pixels, between 0 and 1
      * @return The desired height in pixels, 0 if invalid ratio
      */
     public static int precentWindowY(double percent){


        if(percent < 0 || percent > 1)
            return 0;

        return (int) Math.floor(percent * WINDOW_HEIGHT);
    }

    public static void toggleClass(boolean enable, Node node, String className){

          if(enable){

            // Dont addRowToTable if already added
            if(!node.getStyleClass().contains(className))
                node.getStyleClass().add(className);
        } else{
            node.getStyleClass().remove(className);
        }
    }

}
