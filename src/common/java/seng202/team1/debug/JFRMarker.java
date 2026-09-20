package seng202.team1.debug;

import jdk.jfr.Event;
import jdk.jfr.Label;

public class JFRMarker extends Event {

    @Label("Marker Name")
    public final String name;

    public JFRMarker(String name) {
        this.name = name;
        this.begin();
    }
}
