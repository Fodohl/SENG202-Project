package seng202.team1.data;

/**
 * A record representing trash or waste that a user can log. It records the waste's
 * category, its type, and weight (optional).
 *
 * @author Marcus Demafeliz
 */
public record Trash(int id, TrashCategory trashCategory, TrashType trashType, int trashWeight) {

    public Trash {
    }

    /**
     * Constructor for Trash a user can log without a specified weight
     *
     * @param trashCategory the trash category
     * @param trashType the trash type
     */
    public Trash(TrashCategory trashCategory, TrashType trashType) {
        this(-1, trashCategory, trashType, -1);
    }

    public Trash(TrashCategory trashCategory, TrashType trashType, int trashWeight) {
        this(-1, trashCategory, trashType, trashWeight);
    }

    @Override
    public String toString() {
        return String.format("Trash{%d, %s, %s, %d}", id, trashCategory.toString(), trashType.toString(), trashWeight);
    }
}
