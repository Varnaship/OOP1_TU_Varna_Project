package starwars.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import starwars.model.Jedi;
import starwars.model.JediRank;
import starwars.model.Universe;

/**
 * Reads a Universe from a text file in the required format.
 * Accepts both dot (.) and comma (,) as decimal separator for strength.
 * <p>
 * Чете Universe от текстов файл в необходимия формат.
 * Приема както точка (.), така и запетая (,) като десетичен разделител за сила.
 */
public class UniverseFileReader {
    private static final String PLANET_PREFIX = "PLANET|";
    private static final String JEDI_PREFIX = "JEDI|";
    private static final String END_PLANET = "END_PLANET";

    /**
     * Reads the entire Universe from the specified file.
     *
     * @param file the file path
     * @return a populated Universe object (empty if file does not exist)
     * @throws IOException if the file format is invalid or an I/O error occurs
     */
    public Universe read(Path file) throws IOException {
        Universe universe = new Universe();
        if (!Files.exists(file)) {
            return universe;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name())) {
            String currentPlanet = null;
            List<JediRecord> currentJedis = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

                if (line.startsWith(PLANET_PREFIX)) {
                    if (currentPlanet != null) throw new IOException("Invalid file format.");
                    currentPlanet = line.substring(PLANET_PREFIX.length());
                    if (!universe.addPlanet(currentPlanet)) throw new IOException("Duplicate planet in file.");
                    continue;
                }

                if (line.startsWith(JEDI_PREFIX)) {
                    if (currentPlanet == null) throw new IOException("Jedi row without planet.");
                    currentJedis.add(parseJedi(line));
                    continue;
                }

                if (END_PLANET.equals(line)) {
                    if (currentPlanet == null) throw new IOException("Unexpected END_PLANET.");
                    for (JediRecord record : currentJedis) {
                        Jedi jedi = new Jedi(record.name, record.rank, record.age,
                                record.saberColor, record.strength);
                        if (!universe.createJedi(currentPlanet, jedi)) {
                            throw new IOException("Invalid or duplicate Jedi in file.");
                        }
                    }
                    currentPlanet = null;
                    currentJedis.clear();
                    continue;
                }

                throw new IOException("Unknown row in file.");
            }
            if (currentPlanet != null) throw new IOException("Unclosed planet block.");
        } catch (RuntimeException ex) {
            throw new IOException("Invalid file content.", ex);
        }
        return universe;
    }

    /**
     * Parses a JEDI row. Supports both dot and comma as decimal separator for strength.
     *
     * @param row the raw line starting with "JEDI|"
     * @return a JediRecord containing the parsed data
     * @throws IOException if the row is malformed
     */
    private JediRecord parseJedi(String row) throws IOException {
        String payload = row.substring(JEDI_PREFIX.length());
        String[] parts = payload.split("\\|", -1);
        if (parts.length != 5) throw new IOException("Invalid Jedi row.");

        String name = parts[0];
        JediRank rank = JediRank.fromInput(parts[1]);
        int age = Integer.parseInt(parts[2]);
        String saberColor = parts[3];
        double strength = Double.parseDouble(parts[4].replace(',', '.'));
        return new JediRecord(name, rank, age, saberColor, strength);
    }

    private static class JediRecord {
        final String name;
        final JediRank rank;
        final int age;
        final String saberColor;
        final double strength;
        JediRecord(String name, JediRank rank, int age, String saberColor, double strength) {
            this.name = name; this.rank = rank; this.age = age;
            this.saberColor = saberColor; this.strength = strength;
        }
    }
}