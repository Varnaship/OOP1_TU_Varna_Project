public enum JediRank {
    YOUNGLING,
    INITIATE,
    PADAWAN,
    KNIGHT_ASPIRANT,
    KNIGHT,
    MASTER,
    BATTLE_MASTER,
    GRAND_MASTER;

    public boolean canPromote() {
        return this != GRAND_MASTER;
    }

    public boolean canDemote() {
        return this != YOUNGLING;
    }

    public JediRank promote() {
        if (!canPromote()) return this;
        return values()[ordinal() + 1];
    }

    public JediRank demote() {
        if (!canDemote()) return this;
        return values()[ordinal() - 1];
    }
}