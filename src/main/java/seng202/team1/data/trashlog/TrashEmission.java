package seng202.team1.data.trashlog;

public record TrashEmission(String id, String displayName, double weightGrams, double treatedFactor, double litteredFactor){

    public TrashEmission {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be empty");
        }
    }
}
