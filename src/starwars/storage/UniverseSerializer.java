package starwars.storage;

import java.io.IOException;
import java.nio.file.Path;
import starwars.model.Universe;

/**
 * Serialization contract for Universe objects.
 * <p>
 * Договор за сериализация на Universe обекти.
 */
public interface UniverseSerializer {
    /**
     * Reads a Universe from the given file.
     *
     * @param file the path to the file
     * @return reconstructed Universe
     * @throws IOException if an I/O error occurs
     */
    Universe read(Path file) throws IOException;

    /**
     * Writes a Universe to the given file.
     *
     * @param file     the path to the file
     * @param universe the Universe to save
     * @throws IOException if an I/O error occurs
     */
    void write(Path file, Universe universe) throws IOException;
}