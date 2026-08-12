package seng202.team1.data;

import java.time.Instant;

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

    /// How fill the bin is
    protected double status = 0.0;

    /// When the bin's info was last updated
    protected Instant updated;

    /// Name of the area the bin is located in (@todo resolve to address using api)
    protected String areaName;

    /**
     * Constructor for Bin class
     * @param binID - unique identifier for the bin
     * @param latitude - latitude of the bin
     * @param longitude - longitude of the bin
     * @param type - type of bin (general waste, recycling, organic)
     */
    public Bin(Integer binID, double latitude, double longitude, WasteType type, Instant updated, String areaName) {
        this.binID = binID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.updated = updated;
        this.areaName = areaName;
    }

    /**
     * Get the bin's ID
     * @return Bin ID
     */
    public Integer getID() {
        return binID;
    }

    /**
     * Get the bin's latitude
     * @return Latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the bin's longitude
     * @return Longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the bin's type
     * @return Bin Type
     */
    public WasteType getType() {
        return type;
    }

    /**
     * Get the bin's status
     * @return Bin status
     */
    public Double getStatus() {
        return status;
    }

    /**
     * Get the bin's last update time
     * @return Bin last updated time
     */
    public Instant getUpdated() {
        return updated;
    }

    /**
     * Get the bin's general area of location
     * @return Bin general area of location
     */
    public String getAreaName() {
        return areaName;
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