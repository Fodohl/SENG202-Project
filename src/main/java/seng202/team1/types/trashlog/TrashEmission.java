package seng202.team1.types.trashlog;

/**
 * Emission data for one {@link TrashType}, loaded from trash_emissions.json.
 *
 * @param id              The {@link TrashType} name this entry is for
 * @param displayName     Name shown to the user
 * @param weightGrams     Default weight
 * @param treatedFactor   kg CO2e per kg when recycled or composted
 * @param litteredFactor  kg CO2e per kg when landfilled
 */
public record TrashEmission(String id, String displayName, double weightGrams, double treatedFactor, double litteredFactor){

    public TrashEmission {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        if (weightGrams < 0) {
            throw new IllegalArgumentException("weightGrams cannot be negative");
        }
        if (!Double.isFinite(treatedFactor) || !Double.isFinite(litteredFactor)) {
            throw new IllegalArgumentException("emission factors must be finite");
        }
    }
}
