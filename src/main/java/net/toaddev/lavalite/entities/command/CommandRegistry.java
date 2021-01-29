package net.toaddev.lavalite.entities.command;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;

public class CommandRegistry
{
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandRegistry.class);
    public static HashMap<String, Command> registry = new HashMap<>();
    public static void registerCommand(Command command)
    {
        logger.info("Registered the command" + " " + command.getName() + ".");
        registry.put(command.getName(), command);
    }
    public static void registerAlias(String command, String alias)
    {
        logger.info("Registered the alias" + " " + alias + ".");
        registry.put(alias, registry.get(command));
    }
    public static Command getCommand(String name)
    {
        return registry.get(name);
    }
    public static int getSize()
    {
        return registry.size();
    }
    public static Set<String> getRegisteredCommandsAndAliases()
    {
        return registry.keySet();
    }
}