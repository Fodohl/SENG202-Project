package seng202.team1.data_managers;

/**
 * BinObject
 * Class represents a single bin and its properties
 * @author Stanley Easthope
 */

public class BinObject {
    public enum BinType {
        GENERAL_WASTE,
        RECYCLING,
        ORGANIC
    }

    public enum BinStatus {
        INCORRECT_LOCATION,
        FULL,
        PARTIAL,
        EMPTY
    }

    private int binID;
    private double latitude;
    private double longitude;
    private BinType type;
    private BinStatus status;
    private int numIncorrectLocations;

    /**
     * Constructor for BinObject class
     * @param binID - unique identifier for the bin
     * @param latitude - latitude of the bin
     * @param longitude - longitude of the bin
     * @param type - type of bin (general waste, recycling, organic)
     */
    public BinObject(int binID, double latitude, double longitude, BinType type) {
        this.binID = binID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.status = BinStatus.EMPTY;
        this.numIncorrectLocations = 0;
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

    /**
     * Get the bin's status
     * @return BinStatus
     */
    public BinStatus getBinStatus() { return status; }


    /**
     * Set the bin's status
     */
    public void setBinStatus(BinStatus status) { this.status = status; }

    /**
     * Get the incorrect locations
     * @return numIncorrectLocations
     */

    public int getNumIncorrectLocations() { return numIncorrectLocations; }

    /**
     * Increment incorrect locations
     */

    public void incrementNumIncorrectLocations() { this.numIncorrectLocations++; }

    /**
     * Reset incorrect locations
     */

    public void resetNumIncorrectLocations() { this.numIncorrectLocations = 0; }

    /**
     * Returns string representation of a bin object with binID, latitude and longitude, type and status
     * @return a string representation of a bin
     */

    @Override
    public String toString() {
        return "BinObject{" +
                "binID=" + binID +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                ", status=" + status + "}";
    }

}