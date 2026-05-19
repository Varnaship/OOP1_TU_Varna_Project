package starwars;

import java.util.Scanner;
import starwars.app.CommandProcessor;
import starwars.app.FileSession;
import starwars.storage.TextUniverseSerializer;

/**
 * Entry point of the Star Wars Universe Management application.
 * <p>
 * Начална точка на приложението за управление на Вселената Star Wars.
 */
public class Main {

    /**
     * Starts the application: creates a file session, command processor,
     * and reads commands from standard input until "exit".
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        FileSession session = new FileSession(new TextUniverseSerializer());
        CommandProcessor processor = new CommandProcessor(session);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String line;
            if (!scanner.hasNextLine()) {
                break;
            }
            line = scanner.nextLine();
            running = processor.process(line);
        }
    }
}