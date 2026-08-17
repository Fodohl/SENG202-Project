package seng202.team1.data.route;

public class RouteInstruction {

    private String text;
    private String type;
    private double distance;
    private double time;

    public RouteInstruction() {
    }

    public RouteInstruction(String text, String type, double distance, double time) {
        this.text = text;
        this.type = type;
        this.distance = distance;
        this.time = time;
    }

    public String getText() {
        text = text.replace("+", " ");
        return text;
    }

    public String getType() {
        return type;
    }

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%f,%f",getText(),type,distance,time);
    }
}
