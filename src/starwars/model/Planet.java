package starwars.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a planet that can host multiple Jedi.
 * Each Jedi is stored by name (unique within the planet).
 * <p>
 * Представлява планета, която може да приюти множество джедаи.
 */
public class Planet {
    private final String name;
    private final Map<String, Jedi> jedisByName = new LinkedHashMap<>();

    /**
     * Constructs a planet with the given name.
     * Name must be non‑empty and cannot contain the '|' character.
     *
     * @param name planet name
     * @throws IllegalArgumentException if name is invalid
     */
    public Planet(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Planet name is required.");
        }
        if (name.contains("|")) {
            throw new IllegalArgumentException("Planet name contains invalid symbol.");
        }
        this.name = name;
    }

    public String getName() { return name; }

    /**
     * Adds a Jedi to the planet.
     *
     * @param jedi the Jedi to add
     * @return true if added, false if a Jedi with the same name already exists
     */
    public boolean addJedi(Jedi jedi) {
        if (jedisByName.containsKey(jedi.getName())) return false;
        jedisByName.put(jedi.getName(), jedi);
        return true;
    }

    /**
     * Removes a Jedi by name.
     *
     * @param jediName the name of the Jedi to remove
     * @return Optional containing the removed Jedi, or empty if not found
     */
    public Optional<Jedi> removeJedi(String jediName) {
        return Optional.ofNullable(jedisByName.remove(jediName));
    }

    /**
     * Retrieves a Jedi by name.
     *
     * @param jediName the name of the Jedi
     * @return Optional containing the Jedi, or empty if not found
     */
    public Optional<Jedi> getJedi(String jediName) {
        return Optional.ofNullable(jedisByName.get(jediName));
    }

    /**
     * Returns an unmodifiable collection of all Jedi on the planet.
     *
     * @return collection of Jedi
     */
    public Collection<Jedi> getJedis() {
        return Collections.unmodifiableList(new ArrayList<>(jedisByName.values()));
    }

    /**
     * Returns a list of Jedi sorted for planet printing:
     * first by rank (ascending ordinal), then by name alphabetically.
     *
     * @return sorted list of Jedi
     */
    public List<Jedi> sortedForPlanetPrint() {
        List<Jedi> sorted = new ArrayList<>(jedisByName.values());
        sorted.sort((a, b) -> {
            int byRank = Integer.compare(a.getRank().ordinal(), b.getRank().ordinal());
            if (byRank != 0) return byRank;
            return a.getName().compareTo(b.getName());
        });
        return sorted;
    }
}