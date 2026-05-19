package starwars.app;

/**
 * Prints the list of supported commands.
 * <p>
 * Отпечатва списъка с поддържани команди.
 */
public class HelpPrinter {

    /**
     * Displays all commands and their syntax.
     */
    public void print() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>");
        System.out.println("close");
        System.out.println("save");
        System.out.println("save as <file>");
        System.out.println("add_planet <planet_name>");
        System.out.println("create_jedi <planet_name> <jedi_name> <jedi_rank> <jedi_age> <saber_color> <jedi_strength>");
        System.out.println("removeJedi <jedi_name> <planet_name>");
        System.out.println("promote_jedi <jedi_name> <multiplier>");
        System.out.println("demote_jedi <jedi_name> <multiplier>");
        System.out.println("get_strongest_jedi <planet_name>");
        System.out.println("get_youngest_jedi <planet_name> <jedi_rank>");
        System.out.println("get_most_used_saber_color <planet_name> <jedi_rank>");
        System.out.println("get_most_used_saber_color <planet_name>");
        System.out.println("print <planet_name>");
        System.out.println("print <jedi_name>");
        System.out.println("<planet_name> + <planet_name>");
        System.out.println("help");
        System.out.println("exit");
    }
}