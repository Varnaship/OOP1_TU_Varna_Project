package starwars.app;

import java.util.List;
import starwars.model.Universe;

/**
 * Routes domain commands to mutation or query handlers.
 * <p>
 * Насочва домейн командите към манипулаторите за мутации или заявки.
 */
public class DomainCommandHandler {
    private final MutationCommandsHandler mutationHandler;
    private final QueryCommandsHandler queryHandler;

    /**
     * Constructs a DomainCommandHandler with default mutation and query handlers.
     */
    public DomainCommandHandler() {
        this.mutationHandler = new MutationCommandsHandler();
        this.queryHandler = new QueryCommandsHandler();
    }

    /**
     * Handles a tokenized command. First tries mutation handlers, then query handlers.
     * If no handler matches, prints "Unknown command."
     *
     * @param tokens   tokenized command line (first element is command name)
     * @param universe current universe model
     * @return true (program continues, except exit which is handled higher up)
     */
    public boolean handle(List<String> tokens, Universe universe) {
        if (tokens == null || tokens.isEmpty()) {
            System.out.println("Invalid command.");
            return true;
        }

        String command = tokens.get(0);
        if (mutationHandler.handle(command, tokens, universe)) {
            return true;
        }
        if (queryHandler.handle(tokens, universe)) {
            return true;
        }

        System.out.println("Unknown command.");
        return true;
    }
}