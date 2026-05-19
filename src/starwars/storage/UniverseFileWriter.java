package starwars.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;
import starwars.model.Jedi;
import starwars.model.Planet;
import starwars.model.Universe;

/**
 * Writes a Universe to a text file in the required format.
 * Uses US locale to ensure dot (.) as decimal separator.
 * <p>
 * Записва Universe в текстов файл в необходимия формат.
 * Използва US локал, за да гарантира точка (.) като десетичен разделител.
 */
public class UniverseFileWriter {
    private static final String PLANET_PREFIX = "PLANET|";
    private static final String JEDI_PREFIX = "JEDI|";
    private static final String END_PLANET = "END_PLANET";

    /**
     * Writes the entire Universe to the specified file, creating parent directories if needed.
     *
     * @param file     the destination file path
     * @param universe the Universe to save
     * @throws IOException if an I/O error occurs
     */
    public void write(Path file, Universe universe) throws IOException {
        Path parent = file.toAbsolutePath().normalize().getParent();
        if (parent != null) Files.createDirectories(parent);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8))) {
            for (Planet planet : universe.getPlanets()) {
                writer.println(PLANET_PREFIX + planet.getName());
                for (Jedi jedi : planet.getJedis()) {
                    writer.println(String.format(Locale.US,
                            "%s%s|%s|%d|%s|%.15f",
                            JEDI_PREFIX, jedi.getName(), jedi.getRank().name(),
                            jedi.getAge(), jedi.getSaberColor(), jedi.getStrength()));
                }
                writer.println(END_PLANET);
            }
        }
    }
}