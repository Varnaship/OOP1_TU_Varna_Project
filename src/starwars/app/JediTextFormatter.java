package starwars.app;

import starwars.model.Jedi;

/**
 * Formats a Jedi into a human-readable string.
 * <p>
 * Форматира джедай в четим низ.
 */
public class JediTextFormatter {

    /**
     * Returns a string with Jedi attributes: name, rank, age, saber color, strength.
     *
     * @param jedi the Jedi to format
     * @return formatted string
     */
    public String format(Jedi jedi) {
        return String.format(
                "name=%s, rank=%s, age=%d, saber_color=%s, strength=%.6f",
                jedi.getName(),
                jedi.getRank(),
                jedi.getAge(),
                jedi.getSaberColor(),
                jedi.getStrength());
    }
}