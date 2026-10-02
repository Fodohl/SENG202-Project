package seng202.team1.types.bin;

import seng202.team1.database.DAO.BinDAO;
import seng202.team1.types.status.StatusReport;

import java.time.Instant;
import java.util.List;


/**
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
    protected BinCategory type;

    /// How much trash in the bin
    protected BinFullness fullness = BinFullness.UNKNOWN;

    /// When the bin's info was last updated
    protected Instant updated;

    // The bin's photo url
    protected String photoURL;

    /// Name of the area the bin is located in
    protected String areaName;

    /**
     * Constructor for Bin class
     * @param binID - unique identifier for the bin
     * @param latitude - latitude of the bin
     * @param longitude - longitude of the bin
     * @param type - type of bin (general waste, recycling, organic)
     */
    public Bin(Integer binID, double latitude, double longitude, BinCategory type, Instant updated, String photoURL, String areaName, BinFullness fullness) {
        this.binID = binID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.updated = updated;
        this.photoURL = photoURL;
        this.areaName = areaName;
        this.fullness = fullness;
    }

    public Bin(Integer binID, double latitude, double longitude, BinCategory type, Instant updated, String areaName) {
        this(binID, latitude, longitude, type, updated, null, areaName, BinFullness.UNKNOWN);
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
    public BinCategory getCategory() {
        return type;
    }

    /**
     * Get the bin's fullness
     * @return Bin fullness
     */
    public BinFullness getFullness() {
        return fullness;
    }

    /**
     * Get the bin's last update time
     * @return Bin last updated time
     */
    public Instant getUpdated() {
        return updated;
    }


    /**
     * Get the bin's photo URL
     * @return Bin's photo URL
     */
    public String getPhotoURL() {
        return photoURL;
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

    //@todo should NOT be here
    public void updateFullness(BinDAO db, List<StatusReport> reportsForBin) {


        // Average with no values will fault with div by 0
        if(reportsForBin.isEmpty()){
            fullness = BinFullness.UNKNOWN;
            db.update(this);
            return;
        }

        // Floor the average report value
        int avg = (int)reportsForBin.stream()
                                    .map(r -> r.getFullness().ordinal())
                                    .mapToInt(Integer::intValue)
                                    .average()
                                    .getAsDouble();

        // Map the average back to its enum value
        fullness = BinFullness.EMPTY;
        for(BinFullness fullnessType : BinFullness.values())
            if(fullnessType.ordinal() == avg)
                fullness = fullnessType;

        // Store change
        db.update(this);
    }
}