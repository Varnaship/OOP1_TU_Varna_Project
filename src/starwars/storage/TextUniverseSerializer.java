package starwars.storage;

import java.io.IOException;
import java.nio.file.Path;
import starwars.model.Universe;

/**
 * Text‑based implementation of UniverseSerializer.
 * Delegates reading to UniverseFileReader and writing to UniverseFileWriter.
 * <p>
 * Текстова реализация на UniverseSerializer.
 * Делегира четенето на UniverseFileReader и записването на UniverseFileWriter.
 */
public class TextUniverseSerializer implements UniverseSerializer {
    private final UniverseFileReader reader;
    private final UniverseFileWriter writer;

    public TextUniverseSerializer() {
        this.reader = new UniverseFileReader();
        this.writer = new UniverseFileWriter();
    }

    /**
     * Reads a Universe from a file.
     *
     * @param file the path to the file
     * @return reconstructed Universe
     * @throws IOException if reading fails
     */
    @Override
    public Universe read(Path file) throws IOException {
        return reader.read(file);
    }

    /**
     * Writes a Universe to a file.
     *
     * @param file     the path to the file
     * @param universe the Universe to save
     * @throws IOException if writing fails
     */
    @Override
    public void write(Path file, Universe universe) throws IOException {
        writer.write(file, universe);
    }
}