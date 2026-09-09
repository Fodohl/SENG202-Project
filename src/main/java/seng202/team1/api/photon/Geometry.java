package seng202.team1.api.photon;

import java.util.List;

public record Geometry(
        List<Double> coordinates,
        String type
) {}
