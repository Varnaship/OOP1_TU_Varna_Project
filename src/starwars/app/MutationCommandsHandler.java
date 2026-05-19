package starwars.app;

import java.util.List;
import starwars.model.JediRank;
import starwars.model.Universe;

/**
 * Handles mutation commands: add_planet, create_jedi, removeJedi, promote_jedi, demote_jedi.
 * <p>
 * Обработва мутационни команди: add_planet, create_jedi, removeJedi, promote_jedi, demote_jedi.
 */
public class MutationCommandsHandler {

    /**
     * Attempts to handle a mutation command based on its name.
     *
     * @param command  the command name (e.g., "add_planet")
     * @param tokens   full list of tokens (command + arguments)
     * @param universe current universe model
     * @return true if the command was a mutation and was handled, false otherwise
     */
    public boolean handle(String command, List<String> tokens, Universe universe) {
        if ("add_planet".equals(command)) {
            return handleAddPlanet(universe, tokens);
        }
        if ("create_jedi".equals(command)) {
            return handleCreateJedi(universe, tokens);
        }
        if ("removejedi".equals(command) || "remove_jedi".equals(command)) {
            return handleRemoveJedi(universe, tokens);
        }
        if ("promote_jedi".equals(command)) {
            return handlePromoteDemote(universe, tokens, true);
        }
        if ("demote_jedi".equals(command)) {
            return handlePromoteDemote(universe, tokens, false);
        }
        return false;
    }

    /**
     * Handles "add_planet <planet_name>". Adds a new planet if it doesn't exist.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, planet name)
     * @return true (command processed)
     */
    private boolean handleAddPlanet(Universe universe, List<String> tokens) {
        if (tokens.size() != 2) {
            System.out.println("Invalid command.");
            return true;
        }
        boolean added = universe.addPlanet(tokens.get(1));
        if (added) {
            System.out.println(String.format("Planet %s added.", tokens.get(1)));
        } else {
            System.out.println(String.format("Planet %s already exists.", tokens.get(1)));
        }
        return true;
    }

    /**
     * Handles "create_jedi". Expected format:
     * create_jedi <planet_name> <jedi_name> <jedi_rank> <jedi_age> <saber_color> <jedi_strength>
     * Creates a new Jedi with the specified rank. Strength must be between 1 and 2.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, planet, name, rank, age, color, strength)
     * @return true (command processed)
     */
    private boolean handleCreateJedi(Universe universe, List<String> tokens) {
        if (tokens.size() != 7) {
            System.out.println("Invalid command. Expected: create_jedi <planet_name> <jedi_name> <jedi_rank> <jedi_age> <saber_color> <jedi_strength>");
            return true;
        }
        try {
            String planetName = tokens.get(1);
            String jediName = tokens.get(2);
            JediRank rank = JediRank.fromInput(tokens.get(3));
            int age = Integer.parseInt(tokens.get(4));
            String saberColor = tokens.get(5);
            double strength = Double.parseDouble(tokens.get(6));

            if (strength < 1.0 || strength > 2.0) {
                System.out.println("Invalid strength. Must be between 1 and 2.");
                return true;
            }

            boolean created = universe.createJedi(planetName, jediName, rank, age, saberColor, strength);
            if (created) {
                System.out.println(String.format("Jedi %s created.", jediName));
            } else {
                System.out.println("Failed to create Jedi. (Planet missing, duplicate name, or invalid data)");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number format for age or strength.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid rank: " + ex.getMessage());
        }
        return true;
    }

    /**
     * Handles "removeJedi <jedi_name> <planet_name>". Removes a Jedi from a specific planet.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, jedi name, planet name)
     * @return true (command processed)
     */
    private boolean handleRemoveJedi(Universe universe, List<String> tokens) {
        if (tokens.size() != 3) {
            System.out.println("Invalid command.");
            return true;
        }
        boolean removed = universe.removeJedi(tokens.get(1), tokens.get(2));
        if (removed) {
            System.out.println(String.format("Jedi %s removed from %s.", tokens.get(1), tokens.get(2)));
        } else {
            System.out.println("Failed to remove Jedi.");
        }
        return true;
    }

    /**
     * Handles "promote_jedi" and "demote_jedi".
     * Promotes or demotes a Jedi by one rank and updates strength accordingly.
     *
     * @param universe the universe model
     * @param tokens   tokens (command, jedi name, multiplier)
     * @param promote  true for promotion, false for demotion
     * @return true (command processed)
     */
    private boolean handlePromoteDemote(Universe universe, List<String> tokens, boolean promote) {
        if (tokens.size() != 3) {
            System.out.println("Invalid command.");
            return true;
        }
        try {
            double multiplier = Double.parseDouble(tokens.get(2));
            boolean success;
            if (promote) {
                success = universe.promoteJedi(tokens.get(1), multiplier);
            } else {
                success = universe.demoteJedi(tokens.get(1), multiplier);
            }
            if (success) {
                System.out.println(promote ? "Jedi promoted." : "Jedi demoted.");
            } else {
                System.out.println(promote ? "Cannot promote Jedi." : "Cannot demote Jedi.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number format.");
        }
        return true;
    }
}