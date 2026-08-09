package seng202.team1.data_managers;

/**
 * A record representing trash or waste that a user can log. It records the waste's
 * category, its type, and weight (optional).
 *
 * @author Marcus Demafeliz
 */
public record Trash(TrashCategory trashCategory, TrashType trashType, int trashWeight) {

    /**
     * Enum class representing a
     * broad group to categorise trash
     */
    public enum TrashCategory {
        GENERAL_WASTE,
        RECYCLABLE,
        ORGANIC,
        E_WASTE,
    }

    /**
     * Specific item being thrown away
     */
    public enum TrashType {
        PLASTIC_BOTTLE,
        PAPER,
        CARDBOARD_BOX,
        COFFEE_CUP,
        OTHER
    }

    /**
     * Constructor for Trash a user can log with a specified weight
     *
     * @param trashCategory the trash category
     * @param trashType the trash type
     * @param trashWeight the trash weight in grams
     */
    public Trash {
    }

    /**
     * Constructor for Trash a user can log without a specified weight
     *
     * @param trashCategory the trash category
     * @param trashType the trash type
     */
    public Trash(TrashCategory trashCategory, TrashType trashType) {
        this(trashCategory, trashType, 0);
    }

    @Override
    public String toString() {
        return String.format("Trash{%s, %s, %d}", trashCategory.toString(), trashType.toString(), trashWeight);
    }
}
