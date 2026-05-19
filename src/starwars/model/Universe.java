package starwars.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Central container for all planets and Jedi.
 * Maintains a bidirectional mapping between Jedi names and their home planets.
 * Provides all domain operations: add planet, create/remove/promote/demote Jedi, queries, and merging.
 * <p>
 * Централен контейнер за всички планети и джедаи.
 * Поддържа двупосочна връзка между джедай и неговата планета.
 */
public class Universe {
    private final Map<String, Planet> planetsByName = new LinkedHashMap<>();
    private final Map<String, String> jediToPlanet = new HashMap<>();

    /**
     * Adds a new planet to the universe.
     *
     * @param planetName name of the planet (non‑empty, no '|')
     * @return true if added, false if planet already exists or name invalid
     */
    public boolean addPlanet(String planetName) {
        if (planetName == null || planetName.trim().isEmpty()) return false;
        if (planetsByName.containsKey(planetName)) return false;
        try {
            planetsByName.put(planetName, new Planet(planetName));
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a planet exists.
     *
     * @param planetName the planet name
     * @return true if planet exists
     */
    public boolean hasPlanet(String planetName) {
        return planetsByName.containsKey(planetName);
    }

    /**
     * Retrieves a planet by name.
     *
     * @param planetName the planet name
     * @return Optional containing the Planet, or empty if not found
     */
    public Optional<Planet> getPlanet(String planetName) {
        return Optional.ofNullable(planetsByName.get(planetName));
    }

    /**
     * Returns an unmodifiable collection of all planets.
     *
     * @return collection of planets
     */
    public Collection<Planet> getPlanets() {
        return Collections.unmodifiableList(new ArrayList<>(planetsByName.values()));
    }

    /**
     * Creates a new Jedi on the specified planet with the given rank.
     * Strength must be between 1.0 and 2.0.
     *
     * @param planetName  existing planet
     * @param jediName    unique name (no duplicate across the whole universe)
     * @param rank        initial rank
     * @param age         positive integer
     * @param saberColor  non‑empty string, no '|'
     * @param strength    between 1.0 and 2.0
     * @return true if created successfully, false otherwise
     */
    public boolean createJedi(String planetName, String jediName, JediRank rank, int age, String saberColor, double strength) {
        if (Double.isNaN(strength) || Double.isInfinite(strength)) return false;
        if (strength < 1 || strength > 2) return false;
        if (age <= 0 || jediName == null || jediName.trim().isEmpty()
                || saberColor == null || saberColor.trim().isEmpty() || rank == null) {
            return false;
        }
        Jedi jedi;
        try {
            jedi = new Jedi(jediName, rank, age, saberColor, strength);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return createJedi(planetName, jedi);
    }

    /**
     * Adds an already constructed Jedi object to a planet.
     * This method is public so that file readers can add Jedi directly.
     *
     * @param planetName the planet where the Jedi resides
     * @param jedi       the Jedi instance
     * @return true if added successfully, false otherwise (planet missing or duplicate name)
     */
    public boolean createJedi(String planetName, Jedi jedi) {
        if (jedi == null) return false;
        Planet planet = planetsByName.get(planetName);
        if (planet == null || jediToPlanet.containsKey(jedi.getName())) return false;
        if (!planet.addJedi(jedi)) return false;
        jediToPlanet.put(jedi.getName(), planetName);
        return true;
    }

    /**
     * Removes a Jedi from a specific planet.
     *
     * @param jediName   the Jedi's name
     * @param planetName the planet where the Jedi is located
     * @return true if removed, false if not found
     */
    public boolean removeJedi(String jediName, String planetName) {
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return false;
        Optional<Jedi> removed = planet.removeJedi(jediName);
        if (!removed.isPresent()) return false;
        jediToPlanet.remove(jediName);
        return true;
    }

    /**
     * Retrieves a Jedi by name (across all planets).
     *
     * @param jediName the Jedi's name
     * @return Optional containing the Jedi, or empty if not found
     */
    public Optional<Jedi> getJediByName(String jediName) {
        String planetName = jediToPlanet.get(jediName);
        if (planetName == null) return Optional.empty();
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return Optional.empty();
        return planet.getJedi(jediName);
    }

    /**
     * Returns the name of the planet where a given Jedi resides.
     *
     * @param jediName the Jedi's name
     * @return Optional containing the planet name, or empty if not found
     */
    public Optional<String> getJediPlanetName(String jediName) {
        return Optional.ofNullable(jediToPlanet.get(jediName));
    }

    /**
     * Promotes a Jedi by name: increases rank by one and boosts strength.
     *
     * @param jediName   the Jedi's name
     * @param multiplier positive multiplier
     * @return true if promotion succeeded
     */
    public boolean promoteJedi(String jediName, double multiplier) {
        if (Double.isNaN(multiplier) || Double.isInfinite(multiplier)) return false;
        Optional<Jedi> jedi = getJediByName(jediName);
        if (!jedi.isPresent()) return false;
        return jedi.get().promote(multiplier);
    }

    /**
     * Demotes a Jedi by name: decreases rank by one and reduces strength.
     *
     * @param jediName   the Jedi's name
     * @param multiplier positive multiplier
     * @return true if demotion succeeded
     */
    public boolean demoteJedi(String jediName, double multiplier) {
        if (Double.isNaN(multiplier) || Double.isInfinite(multiplier)) return false;
        Optional<Jedi> jedi = getJediByName(jediName);
        if (!jedi.isPresent()) return false;
        return jedi.get().demote(multiplier);
    }

    /**
     * Finds the strongest Jedi (by strength) on a given planet.
     *
     * @param planetName the planet name
     * @return Optional containing the strongest Jedi, or empty if no Jedi
     */
    public Optional<Jedi> strongestJedi(String planetName) {
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return Optional.empty();
        Jedi strongest = null;
        for (Jedi jedi : planet.getJedis()) {
            if (strongest == null || jedi.getStrength() > strongest.getStrength()) {
                strongest = jedi;
            }
        }
        return Optional.ofNullable(strongest);
    }

    /**
     * Finds the youngest Jedi of a given rank on a planet.
     * In case of age tie, selects alphabetically smallest name.
     *
     * @param planetName the planet name
     * @param rank       the required rank
     * @return Optional containing the youngest Jedi, or empty if none found
     */
    public Optional<Jedi> youngestByRank(String planetName, JediRank rank) {
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return Optional.empty();
        Jedi youngest = null;
        for (Jedi jedi : planet.getJedis()) {
            if (jedi.getRank() != rank) continue;
            if (youngest == null || jedi.getAge() < youngest.getAge()
                    || (jedi.getAge() == youngest.getAge() && jedi.getName().compareTo(youngest.getName()) < 0)) {
                youngest = jedi;
            }
        }
        return Optional.ofNullable(youngest);
    }

    /**
     * Finds the most frequently used lightsaber color among Jedi of a specific rank on a planet.
     * If multiple colors have the same max count, returns the alphabetically smallest.
     *
     * @param planetName the planet name
     * @param rank       the rank to filter by
     * @return Optional containing the color, or empty if no Jedi of that rank exist
     */
    public Optional<String> mostUsedSaberColor(String planetName, JediRank rank) {
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return Optional.empty();
        Map<String, Integer> counts = new HashMap<>();
        for (Jedi jedi : planet.getJedis()) {
            if (jedi.getRank() == rank) {
                counts.put(jedi.getSaberColor(), counts.getOrDefault(jedi.getSaberColor(), 0) + 1);
            }
        }
        return pickMostUsedColor(counts);
    }

    /**
     * Finds the most used lightsaber color only among colors that appear on at least one Grand Master.
     * If multiple colors tie, returns alphabetically smallest.
     *
     * @param planetName the planet name
     * @return Optional containing the color, or empty if no Grand Masters exist
     */
    public Optional<String> mostUsedSaberColorWithGrandMasterRule(String planetName) {
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return Optional.empty();
        Map<String, Boolean> gmColors = new HashMap<>();
        for (Jedi jedi : planet.getJedis()) {
            if (jedi.getRank() == JediRank.GRAND_MASTER) {
                gmColors.put(jedi.getSaberColor(), true);
            }
        }
        if (gmColors.isEmpty()) return Optional.empty();
        Map<String, Integer> counts = new HashMap<>();
        for (Jedi jedi : planet.getJedis()) {
            if (gmColors.containsKey(jedi.getSaberColor())) {
                counts.put(jedi.getSaberColor(), counts.getOrDefault(jedi.getSaberColor(), 0) + 1);
            }
        }
        return pickMostUsedColor(counts);
    }

    /**
     * Returns a sorted list of Jedi on a planet for printing (by rank then name).
     *
     * @param planetName the planet name
     * @return list of Jedi (empty if planet not found)
     */
    public List<Jedi> printPlanetSorted(String planetName) {
        Planet planet = planetsByName.get(planetName);
        if (planet == null) return Collections.emptyList();
        return planet.sortedForPlanetPrint();
    }

    /**
     * Merges the Jedi lists of two planets and returns them sorted alphabetically by name.
     * The original planets are not modified.
     *
     * @param firstPlanet  name of the first planet
     * @param secondPlanet name of the second planet
     * @return merged sorted list, or empty if any planet missing
     */
    public List<Jedi> mergePlanetJedisSortedByName(String firstPlanet, String secondPlanet) {
        Planet first = planetsByName.get(firstPlanet);
        Planet second = planetsByName.get(secondPlanet);
        if (first == null || second == null) return Collections.emptyList();
        List<Jedi> all = new ArrayList<>(first.getJedis());
        all.addAll(second.getJedis());
        all.sort((a, b) -> a.getName().compareTo(b.getName()));
        return all;
    }

    /**
     * Clears all planets and Jedi from the universe.
     */
    public void clear() {
        planetsByName.clear();
        jediToPlanet.clear();
    }

    /**
     * Helper method: picks the color with the highest count; if tie, picks alphabetically smallest.
     *
     * @param counts map color -> frequency
     * @return Optional containing the chosen color, or empty if map is empty
     */
    private Optional<String> pickMostUsedColor(Map<String, Integer> counts) {
        String bestColor = null;
        int bestCount = -1;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            String color = entry.getKey();
            int count = entry.getValue();
            if (count > bestCount || (count == bestCount && color.compareTo(bestColor) < 0)) {
                bestCount = count;
                bestColor = color;
            }
        }
        return Optional.ofNullable(bestColor);
    }
}