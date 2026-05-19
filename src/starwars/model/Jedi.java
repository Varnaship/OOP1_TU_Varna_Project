package starwars.model;

/**
 * Represents a Jedi with name, rank, age, lightsaber color, and strength.
 * Supports promotion and demotion (rank change and strength adjustment).
 * <p>
 * Представлява джедай с име, ранг, възраст, цвят на светлинен меч и сила.
 * Поддържа повишаване и понижаване (промяна на ранг и сила).
 */
public class Jedi {
    private String name;
    private JediRank rank;
    private int age;
    private String saberColor;
    private double strength;

    /**
     * Constructs a new Jedi with the specified attributes.
     *
     * @param name       Jedi's name (non‑null, non‑empty, no '|')
     * @param rank       initial rank
     * @param age        age (positive)
     * @param saberColor lightsaber color (non‑null, non‑empty, no '|')
     * @param strength   strength value (finite, typically 1‑2)
     * @throws IllegalArgumentException if any validation fails
     */
    public Jedi(String name, JediRank rank, int age, String saberColor, double strength) {
        setName(name);
        setRank(rank);
        setAge(age);
        setSaberColor(saberColor);
        setStrength(strength);
    }

    /**
     * Returns the Jedi's name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Returns the Jedi's rank.
     *
     * @return the rank
     */
    public JediRank getRank() { return rank; }

    /**
     * Returns the Jedi's age.
     *
     * @return the age
     */
    public int getAge() { return age; }

    /**
     * Returns the lightsaber color.
     *
     * @return the saber color
     */
    public String getSaberColor() { return saberColor; }

    /**
     * Returns the strength value.
     *
     * @return the strength
     */
    public double getStrength() { return strength; }

    /**
     * Sets the Jedi's name.
     *
     * @param name new name (non‑null, non‑empty, no '|')
     * @throws IllegalArgumentException if invalid
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Jedi name is required.");
        }
        if (name.contains("|")) {
            throw new IllegalArgumentException("Jedi name contains invalid symbol.");
        }
        this.name = name;
    }

    /**
     * Sets the Jedi's rank.
     *
     * @param rank new rank (cannot be null)
     * @throws IllegalArgumentException if rank is null
     */
    public void setRank(JediRank rank) {
        if (rank == null) {
            throw new IllegalArgumentException("Rank cannot be null.");
        }
        this.rank = rank;
    }

    /**
     * Sets the Jedi's age.
     *
     * @param age new age (must be positive)
     * @throws IllegalArgumentException if age {@code <=} 0
     */
    public void setAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Jedi age must be positive.");
        }
        this.age = age;
    }

    /**
     * Sets the lightsaber color.
     *
     * @param saberColor new color (non‑null, non‑empty, no '|')
     * @throws IllegalArgumentException if invalid
     */
    public void setSaberColor(String saberColor) {
        if (saberColor == null || saberColor.trim().isEmpty()) {
            throw new IllegalArgumentException("Saber color is required.");
        }
        if (saberColor.contains("|")) {
            throw new IllegalArgumentException("Saber color contains invalid symbol.");
        }
        this.saberColor = saberColor;
    }

    /**
     * Sets the strength value.
     *
     * @param strength new strength (must be finite)
     * @throws IllegalArgumentException if strength is NaN or infinite
     */
    public void setStrength(double strength) {
        if (Double.isNaN(strength) || Double.isInfinite(strength)) {
            throw new IllegalArgumentException("Jedi strength must be finite.");
        }
        this.strength = strength;
    }

    /**
     * Promotes the Jedi to the next rank and increases strength by multiplier * strength.
     *
     * @param multiplier positive multiplier
     * @return true if promotion succeeded, false otherwise
     */
    public boolean promote(double multiplier) {
        if (Double.isNaN(multiplier) || Double.isInfinite(multiplier)) return false;
        if (multiplier <= 0 || !rank.canPromote()) return false;
        rank = rank.promoted();
        strength += multiplier * strength;
        return true;
    }

    /**
     * Demotes the Jedi to the previous rank and decreases strength by multiplier * strength.
     *
     * @param multiplier positive multiplier
     * @return true if demotion succeeded, false otherwise
     */
    public boolean demote(double multiplier) {
        if (Double.isNaN(multiplier) || Double.isInfinite(multiplier)) return false;
        if (multiplier <= 0 || !rank.canDemote()) return false;
        rank = rank.demoted();
        strength -= multiplier * strength;
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                "Jedi{name='%s', rank=%s, age=%d, saber_color='%s', strength=%.6f}",
                name, rank, age, saberColor, strength);
    }
}