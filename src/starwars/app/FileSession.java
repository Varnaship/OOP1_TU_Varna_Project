package starwars.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import starwars.model.Universe;
import starwars.storage.UniverseSerializer;

/**
 * Manages file operations (open, close, save, saveAs) for a Universe.
 * Keeps track of the current file path and open state.
 * <p>
 * Управлява файловите операции (отваряне, затваряне, запазване, запазване като) за Universe.
 */
public class FileSession {
    private final UniverseSerializer serializer;
    private Universe universe = new Universe();
    private Path currentFile;
    private boolean open;

    /**
     * Constructs a FileSession with the given serializer.
     *
     * @param serializer the serializer used for reading/writing Universe objects
     */
    public FileSession(UniverseSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Returns whether a file is currently open.
     *
     * @return true if a file is open, false otherwise
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Returns the current Universe (may be empty if no file opened or after close).
     *
     * @return the current Universe instance
     */
    public Universe getUniverse() {
        return universe;
    }

    /**
     * Opens a file: loads its content; creates empty file if missing.
     * The file path is normalised to an absolute path.
     *
     * @param filePath path to file (relative or absolute)
     * @return success message
     * @throws IOException if an I/O error occurs (invalid path, read error, etc.)
     */
    public String open(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IOException("File path is empty.");
        }
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        boolean exists = Files.exists(path);
        universe = serializer.read(path);
        if (!exists) {
            serializer.write(path, universe);
        }
        currentFile = path;
        open = true;
        return String.format("Successfully opened %s", path.getFileName());
    }

    /**
     * Closes the current file and resets the universe to empty.
     *
     * @return success message
     */
    public String close() {
        String fileName = currentFile != null ? currentFile.getFileName().toString() : "file";
        universe = new Universe();
        currentFile = null;
        open = false;
        return String.format("Successfully closed %s", fileName);
    }

    /**
     * Saves the current universe to the currently open file.
     *
     * @return success message or "No file is currently open."
     * @throws IOException if an I/O error occurs during writing
     */
    public String save() throws IOException {
        if (!open || currentFile == null) {
            return "No file is currently open.";
        }
        serializer.write(currentFile, universe);
        return String.format("Successfully saved %s", currentFile.getFileName());
    }

    /**
     * Saves the current universe to a new file (path provided). The new file becomes the current file.
     *
     * @param filePath destination file path
     * @return success message
     * @throws IOException if an I/O error occurs or the path is invalid
     */
    public String saveAs(String filePath) throws IOException {
        if (!open) {
            return "No file is currently open.";
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IOException("File path is empty.");
        }
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        serializer.write(path, universe);
        currentFile = path;
        return String.format("Successfully saved %s", path.getFileName());
    }
}