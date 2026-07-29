package seng202.team0.data_managers;

public class BinObject {
    public enum BinType {
        GENERAL_WASTE,
        RECYCLING,
        ORGANIC
    }
    private int binID;
    private double latitude;
    private double longitude;
    private BinType type;

    public BinObject(int binID, double longitude, double latitude, BinType type) {
        this.binID = binID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }
    public int getBinID() {
        return binID;
    }
    public double getBinLatitude() {
        return latitude;
    }
    public double getBinLongitude() {
        return longitude;
    }
    public BinType getBinType() {
        return type;
    }
    @Override
    public String toString() {
        return "BinObject{" +
                "binID=" + binID +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                '}';
    }

}