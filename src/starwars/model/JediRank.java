package starwars.model;

import java.util.Locale;

/**
 * Enumeration of Jedi ranks in increasing order.
 * <p>
 * Изброяване на ранговете на джедаите във възходящ ред.
 */
public enum JediRank {
    YOUNGLING,
    INITIATE,
    PADAWAN,
    KNIGHT_ASPIRANT,
    KNIGHT,
    MASTER,
    BATTLE_MASTER,
    GRAND_MASTER;

    /**
     * Converts a string (case‑insensitive, hyphens allowed) to a JediRank.
     *
     * @param value input string
     * @return corresponding rank
     * @throws IllegalArgumentException if not a valid rank
     */
    public static JediRank fromInput(String value) {
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        return JediRank.valueOf(normalized);
    }

    /**
     * @return true if promotion to a higher rank is possible (rank is not GRAND_MASTER)
     */
    public boolean canPromote() { return this != GRAND_MASTER; }

    /**
     * @return true if demotion to a lower rank is possible (rank is not YOUNGLING)
     */
    public boolean canDemote() { return this != YOUNGLING; }

    /**
     * Returns the next higher rank.
     *
     * @return promoted rank
     * @throws IllegalStateException if cannot promote
     */
    public JediRank promoted() {
        if (!canPromote()) throw new IllegalStateException("Rank cannot be promoted.");
        return values()[ordinal() + 1];
    }

    /**
     * Returns the next lower rank.
     *
     * @return demoted rank
     * @throws IllegalStateException if cannot demote
     */
    public JediRank demoted() {
        if (!canDemote()) throw new IllegalStateException("Rank cannot be demoted.");
        return values()[ordinal() - 1];
    }

    /**
     * Returns the rank name with hyphens instead of underscores (e.g., "KNIGHT-ASPIRANT").
     *
     * @return formatted rank string
     */
    @Override
    public String toString() {
        return name().replace('_', '-');
    }
}