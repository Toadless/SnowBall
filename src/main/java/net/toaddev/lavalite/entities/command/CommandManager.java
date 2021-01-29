package net.toaddev.lavalite.entities.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;

import static net.toaddev.lavalite.entities.command.init.CommandInitializer.initCommands;

public class CommandManager
{
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager()
    {
        initCommands();
    }

    public void handleCommand(String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        String providedCommandName = args[0].replace(prefix, "");
        Command command = CommandRegistry.getCommand(providedCommandName);
        if (command == null) {
            return;
        }
        CommandEvent ctx = new CommandEvent(event, args, prefix);
        command.process(ctx);
    }
}