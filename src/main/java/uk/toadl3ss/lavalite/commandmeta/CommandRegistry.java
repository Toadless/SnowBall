package uk.toadl3ss.lavalite.commandmeta;

import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

import java.util.HashMap;

public class CommandRegistry {
    // ################################################################################
    // ##                     Command Registry
    // ################################################################################
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandRegistry.class);
    public static HashMap<String, Command> registry = new HashMap<>();
    public static void registerCommand(String name, Command command) {
        logger.info("Registered the command" + " " + name + ".");
        registry.put(name, command);
    }
    public static void registerAlias(String command, String alias) {
        logger.info("Registered the alias" + " " + alias + ".");
        registry.put(alias, registry.get(command));
    }
    public static Command getCommand(String name) {
        return registry.get(name);
    }
}