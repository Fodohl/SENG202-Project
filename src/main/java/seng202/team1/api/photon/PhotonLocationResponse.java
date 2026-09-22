package seng202.team1.api.photon;
import java.util.List;

/**
 * Photo Type: response for a location request
 *
 * @param type The type of response
 * @param features The resolved locations (if any)
 */
public record PhotonLocationResponse (
        String type,
        List<Feature> features
){}


