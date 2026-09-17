package seng202.team1.data.trashlog;

import seng202.team1.data.bin.BinCategory;

/**
 * A record representing trash or waste that a user can log. It records the waste's
 * category, its type, and weight (optional).
 *
 * @todo remove bin can as can be fetched from ID
 * @author Marcus Demafeliz
 */
public record Trash(BinCategory binCategory, TrashType trashType, int trashWeight) {
    
    /**
     * Constructor for Trash a user can log without a specified weight
     *
     * @param binCategory the trash category
     * @param trashType the trash type
     */
    public Trash(BinCategory binCategory, TrashType trashType) {
        this(binCategory, trashType, 0);
    }

    @Override
    public String toString() {
        return String.format("Trash{%s, %s, %d}", binCategory.toString(), trashType.toString(), trashWeight);
    }
}
