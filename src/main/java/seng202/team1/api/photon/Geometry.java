package seng202.team1.api.photon;

import java.util.List;

/**
 * Photon Type: geometric position
 *
 * @param coordinates Position details [longitude, lattice] (warning reverse to TrassApp ordering convention)
 * @param type The type of position
 */
public record Geometry(
        List<Double> coordinates,
        String type
) {}
