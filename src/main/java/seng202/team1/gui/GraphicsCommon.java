package seng202.team1.gui;

import seng202.team1.Main;

public class GraphicsCommon {

    public static final int WINDOW_WIDTH = 1700;
    public static final int WINDOW_HEIGHT = 850;

    public static int precentX(double percent){

        //@todo check bounds
        return (int) Math.floor( percent * WINDOW_WIDTH);
    }

     public static int precentY(double percent){

        //@todo check bounds
        return (int) Math.floor(percent * WINDOW_HEIGHT);
    }

}
