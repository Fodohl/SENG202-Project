package seng202.team1.data.route;

<<<<<<< HEAD
public class RouteInstruction {

    private String text;
    private String type;
    private double distance;
    private double time;

    public RouteInstruction() {
    }

=======
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
>>>>>>> main
    public RouteInstruction(String text, String type, double distance, double time) {
        this.text = text;
        this.type = type;
        this.distance = distance;
        this.time = time;
    }

<<<<<<< HEAD
=======
    /**
     * Get the route's instruction
     *
     * @return The instruction
     */
>>>>>>> main
    public String getText() {
        text = text.replace("+", " ");
        return text;
    }

<<<<<<< HEAD
=======
    /**
     * Get the type of turn
     *
     * @return The type of turn
     */
>>>>>>> main
    public String getType() {
        return type;
    }

<<<<<<< HEAD
=======
    /**
     * Get how long to go this direction
     *
     * @return How long to go this direction
     */
>>>>>>> main
    public double getDistance() {
        return distance;
    }

<<<<<<< HEAD
=======
    /**
     * Get how long this part of the route will take
     *
     * @return How long this part of the route will take
     */
>>>>>>> main
    public double getTime() {
        return time;
    }

<<<<<<< HEAD
=======
    /**
     * Assemble the instruction into a string
     *
     * @return The instruction
     */
>>>>>>> main
    @Override
    public String toString() {
        return String.format("%s,%s,%f,%f",getText(),type,distance,time);
    }
}
