package seng202.team1.common;

public class StringHelpers {

    public static String toTitleCase(String string){
       return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
