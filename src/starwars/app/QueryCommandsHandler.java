package starwars.app;

import java.util.List;
import java.util.Optional;
import starwars.model.Jedi;
import starwars.model.JediRank;
import starwars.model.Universe;

/**
 * Handles query commands: get_strongest_jedi, get_youngest_jedi, get_most_used_saber_color, print, and planet merge.
 * <p>
 * Обработва заявки: get_strongest_jedi, get_youngest_jedi, get_most_used_saber_color, print и сливане на планети.
 */
public class QueryCommandsHandler {
    private final JediTextFormatter formatter;

    /**
     * Constructs a QueryCommandsHandler with a default JediTextFormatter.
     */
    public QueryCommandsHandler() {
        this.formatter = new JediTextFormatter();
    }

    /**
     * Handles a tokenized command. Recognises planet merge ("+"), strongest, youngest,
     * most used color, and print commands.
     *
     * @param tokens   tokenized command line
     * @param universe current universe model
     * @return true if the command was a query and was handled, false otherwise
     */
    public boolean handle(List<String> tokens, Universe universe) {
        if (tokens.size() == 3 && "+".equals(tokens.get(1))) {
            return handlePlanetMerge(universe, tokens.get(0), tokens.get(2));
        }

        String command = tokens.get(0);
        if ("get_strongest_jedi".equals(command)) {
            return handleStrongest(universe, tokens);
        }
        if ("get_youngest_jedi".equals(command)) {
            return handleYoungest(universe, tokens);
        }
        if ("get_most_used_saber_color".equals(command)) {
            return handleMostUsedColor(universe, tokens);
        }
        if ("print".equals(command)) {
            return handlePrint(universe, tokens);
        }
        return false;
    }

    /**
     * Handles "get_strongest_jedi <planet_name>".
     * Prints the Jedi with the highest strength on the planet.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, planet name)
     * @return true
     */
    private boolean handleStrongest(Universe universe, List<String> tokens) {
        if (tokens.size() != 2) {
            System.out.println("Invalid command.");
            return true;
        }
        Optional<Jedi> strongest = universe.strongestJedi(tokens.get(1));
        if (strongest.isPresent()) {
            System.out.println(formatter.format(strongest.get()));
        } else {
            System.out.println("No Jedi found.");
        }
        return true;
    }

    /**
     * Handles "get_youngest_jedi <planet_name> <jedi_rank>".
     * Prints the youngest Jedi of the given rank on the planet.
     * In case of age tie, the alphabetically smallest name is chosen.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, planet, rank)
     * @return true
     */
    private boolean handleYoungest(Universe universe, List<String> tokens) {
        if (tokens.size() != 3) {
            System.out.println("Invalid command.");
            return true;
        }
        try {
            JediRank rank = JediRank.fromInput(tokens.get(2));
            Optional<Jedi> youngest = universe.youngestByRank(tokens.get(1), rank);
            if (youngest.isPresent()) {
                System.out.println(formatter.format(youngest.get()));
            } else {
                System.out.println("No Jedi found.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid rank.");
        }
        return true;
    }

    /**
     * Handles "get_most_used_saber_color". Supports two forms:
     * with rank (3 tokens) and without rank (2 tokens).
     * Without rank, only colors used by at least one GRAND_MASTER are considered.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, planet, [rank])
     * @return true
     */
    private boolean handleMostUsedColor(Universe universe, List<String> tokens) {
        if (tokens.size() == 2) {
            Optional<String> color = universe.mostUsedSaberColorWithGrandMasterRule(tokens.get(1));
            if (color.isPresent()) {
                System.out.println(color.get());
            } else {
                System.out.println("No color found.");
            }
            return true;
        }
        if (tokens.size() == 3) {
            try {
                JediRank rank = JediRank.fromInput(tokens.get(2));
                Optional<String> color = universe.mostUsedSaberColor(tokens.get(1), rank);
                if (color.isPresent()) {
                    System.out.println(color.get());
                } else {
                    System.out.println("No color found.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid rank.");
            }
            return true;
        }
        System.out.println("Invalid command.");
        return true;
    }

    /**
     * Handles "print". If the argument is a planet name, prints all Jedi on that planet
     * sorted by rank then name. If it is a Jedi name, prints the Jedi's details and home planet.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, name)
     * @return true
     */
    private boolean handlePrint(Universe universe, List<String> tokens) {
        if (tokens.size() != 2) {
            System.out.println("Invalid command.");
            return true;
        }
        String value = tokens.get(1);
        if (universe.hasPlanet(value)) {
            System.out.println("Planet: " + value);
            List<Jedi> jedis = universe.printPlanetSorted(value);
            if (jedis.isEmpty()) {
                System.out.println("No Jedi.");
                return true;
            }
            for (Jedi jedi : jedis) {
                System.out.println(formatter.format(jedi));
            }
            return true;
        }

        Optional<Jedi> jedi = universe.getJediByName(value);
        Optional<String> planetName = universe.getJediPlanetName(value);
        if (jedi.isPresent() && planetName.isPresent()) {
            System.out.println(formatter.format(jedi.get()) + ", planet=" + planetName.get());
        } else {
            System.out.println("No matching planet or Jedi.");
        }
        return true;
    }

    /**
     * Handles "<planet1> + <planet2>". Merges the Jedi lists of two planets,
     * sorts them alphabetically by name, and prints each Jedi together with its home planet.
     *
     * @param universe     the universe model
     * @param firstPlanet  first planet name
     * @param secondPlanet second planet name
     * @return true
     */
    private boolean handlePlanetMerge(Universe universe, String firstPlanet, String secondPlanet) {
        if (!universe.hasPlanet(firstPlanet) || !universe.hasPlanet(secondPlanet)) {
            System.out.println("Planet not found.");
            return true;
        }
        List<Jedi> merged = universe.mergePlanetJedisSortedByName(firstPlanet, secondPlanet);
        for (Jedi jedi : merged) {
            Optional<String> planet = universe.getJediPlanetName(jedi.getName());
            if (planet.isPresent()) {
                System.out.println(formatter.format(jedi) + ", planet=" + planet.get());
            } else {
                System.out.println(formatter.format(jedi) + ", planet=unknown");
            }
        }
        return true;
    }
}