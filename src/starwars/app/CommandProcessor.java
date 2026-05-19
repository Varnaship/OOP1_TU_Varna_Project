package starwars.app;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Processes user commands: delegates file commands (open, close, save, save as, help, exit)
 * and domain commands to DomainCommandHandler.
 * <p>
 * Обработва потребителски команди: делегира файлови команди (open, close, save, save as, help, exit)
 * и домейн команди на DomainCommandHandler.
 */
public class CommandProcessor {
    private final FileSession session;
    private final HelpPrinter helpPrinter;
    private final DomainCommandHandler domainCommandHandler;

    /**
     * Constructs a CommandProcessor with the given file session.
     *
     * @param session the file session used for file operations
     */
    public CommandProcessor(FileSession session) {
        this.session = session;
        this.helpPrinter = new HelpPrinter();
        this.domainCommandHandler = new DomainCommandHandler();
    }

    /**
     * Processes a single command line.
     * Recognises built‑in commands (open, close, save, help, exit) and the merge command ("+").
     * Other commands are delegated to the domain handler.
     *
     * @param line raw command line
     * @return false to exit the program, true otherwise
     */
    public boolean process(String line) {
        List<String> tokens = CommandTokenizer.tokenize(line);
        if (tokens.isEmpty()) {
            return true;
        }

        if (tokens.size() == 3 && "+".equals(tokens.get(1))) {
            if (!session.isOpen()) {
                System.out.println("No file is currently open.");
                return true;
            }
            return domainCommandHandler.handle(tokens, session.getUniverse());
        }

        String command = tokens.get(0).toLowerCase(Locale.ROOT);
        tokens.set(0, command);
        switch (command) {
            case "open":
                return handleOpen(tokens);
            case "close":
                return handleClose();
            case "save":
                return handleSave(tokens);
            case "help":
                helpPrinter.print();
                return true;
            case "exit":
                System.out.println("Exiting the program...");
                return false;
            default:
                if (!session.isOpen()) {
                    System.out.println("No file is currently open.");
                    return true;
                }
                return domainCommandHandler.handle(tokens, session.getUniverse());
        }
    }

    /**
     * Handles the "open" command. Expects exactly one argument (file path).
     * Opens or creates the file and loads its content into memory.
     *
     * @param tokens tokenized command line
     * @return true (program continues)
     */
    private boolean handleOpen(List<String> tokens) {
        if (tokens.size() != 2) {
            System.out.println("Invalid command.");
            return true;
        }
        try {
            System.out.println(session.open(tokens.get(1)));
        } catch (IOException ex) {
            System.out.println("Error while opening file: " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Handles the "close" command. Closes the currently open file and discards in‑memory changes.
     *
     * @return true (program continues)
     */
    private boolean handleClose() {
        if (!session.isOpen()) {
            System.out.println("No file is currently open.");
            return true;
        }
        System.out.println(session.close());
        return true;
    }

    /**
     * Handles the "save" command.
     * Two forms: "save" (saves to current file) and "save as <file>" (saves to a new file).
     *
     * @param tokens tokenized command line
     * @return true (program continues)
     */
    private boolean handleSave(List<String> tokens) {
        try {
            if (tokens.size() == 1) {
                System.out.println(session.save());
                return true;
            }
            if (tokens.size() == 3 && "as".equalsIgnoreCase(tokens.get(1))) {
                System.out.println(session.saveAs(tokens.get(2)));
                return true;
            }
            System.out.println("Invalid command.");
        } catch (IOException ex) {
            System.out.println("Error while saving file: " + ex.getMessage());
        }
        return true;
    }
}