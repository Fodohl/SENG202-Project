package seng202.team1.data.route;

/**
 * Represents a routing step from leaflet
 */
public class RouteInstruction {

    /// The instruction
    private String text;

    /// The type of turn
    private String type;

    /// How long to go this direction
    private double distance;

    /// How long this part of the route will take
    private double time;

    /**
     * Empty constructor for GSON
     */
    public RouteInstruction() {
    }

    /**
     * Construct a new route instruction
     *
     * @param text The instruction
     * @param type The type of turn
     * @param distance How long to go this direction
     * @param time How long this part of the route will take
     */
    public RouteInstruction(String text, String type, double distance, double time) {
        this.text = text;
        this.type = type;
        this.distance = distance;
        this.time = time;
    }

    /**
     * Get the route's instruction
     * <p>
     * Warning: assumes leaflet's style of whitespaces escaped as '+'
     *
     * @return The instruction
     */
    public String getText() {
        text = text.replace("+", " ");
        return text;
    }

    /**
     * Get the type of turn
     *
     * @return The type of turn
     */
    public String getType() {
        return type;
    }

    /**
     * Get how long to go this direction
     *
     * @return How long to go this direction
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get how long this part of the route will take
     *
     * @return How long this part of the route will take
     */
    public double getTime() {
        return time;
    }

    /**
     * Assemble the instruction into a string
     *
     * @return The instruction
     */
    @Override
    public String toString() {
        return String.format("%s,%s,%f,%f",getText(),type,distance,time);
    }
}
