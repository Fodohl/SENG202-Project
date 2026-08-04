package seng202.team1.data;

/**
 * BinObject
 * Class represents a single bin and its properties
 * @author Stanley Easthope
 */

public class Bin {
    public enum BinType {
        GENERAL_WASTE,
        RECYCLING,
        ORGANIC
    }

    private int binID;
    private double latitude;
    private double longitude;
    private BinType type;

    /**
     * Constructor for BinObject class
     * @param binID - unique identifier for the bin
     * @param latitude - latitude of the bin
     * @param longitude - longitude of the bin
     * @param type - type of bin (general waste, recycling, organic)
     */
    public Bin(int binID, double latitude, double longitude, BinType type) {
        this.binID = binID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    /**
     * Get the bin's ID
     * @return Bin ID
     */
    public int getBinID() {
        return binID;
    }

    /**
     * Get the bin's latitude
     * @return Latitude
     */
    public double getBinLatitude() {
        return latitude;
    }
    /**
     * Get the bin's longitude
     * @return Longitude
     */
    public double getBinLongitude() {
        return longitude;
    }
    /**
     * Get the bin's type
     * @return Bin Type
     */
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