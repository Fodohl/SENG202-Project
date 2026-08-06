package seng202.team1.data;

/**
 * BinObject
 * Class represents a single bin and its properties
 * @author Stanley Easthope
 */

public class Bin {

    /// The bin's unique identifier number
    protected Integer binID;

    /// The bin's latitudinal location
    protected double latitude;

    /// The bin's longitudinal location
    protected double longitude;

    /// The type of rubish the bin takes
    protected WasteType type;

    /**
     * Constructor for Bin class
     * @param binID - unique identifier for the bin
     * @param latitude - latitude of the bin
     * @param longitude - longitude of the bin
     * @param type - type of bin (general waste, recycling, organic)
     */
    public Bin(Integer binID, double latitude, double longitude, WasteType type) {
        this.binID = binID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    /**
     * Get the bin's ID
     * @return Bin ID
     */
    public Integer getBinID() {
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
    public WasteType getBinType() {
        return type;
    }

    @Override
    public String toString() {
        return "Bin{" +
                "binID=" + binID +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                '}';
    }

}