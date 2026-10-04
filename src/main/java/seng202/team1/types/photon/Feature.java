package seng202.team1.types.photon;

/**
 * Photon Type: geometric feature
 *
 * @param type The type of feature this is
 * @param geometry The positional geometry of the feature
 * @param properties Feature specific properties
 */
public record Feature(
        String type,
        Geometry geometry,
        Properties properties
) {
}
