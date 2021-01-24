package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandRegistry;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.entities.command.init.CommandInitializer;
import uk.toadl3ss.lavalite.data.Config;

public class RegistryCommand extends Command
{
    public RegistryCommand()
    {
        super("registry", null);
        addFlag(CommandFlags.DEVELOPER_ONLY);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        if (!Config.INS.getDevelopment())
        {
            event.getChannel().sendMessage("This command can only be ran in development.").queue();
            return;
        }
        if (args.length < 2)
        {
            event.getChannel().sendMessage("Please provide an option. `clear` | `rebuild` | `log`").queue();
            return;
        }
        if (args[1].equals("clear"))
        {
            CommandRegistry.registry.clear();
            CommandRegistry.logger.info("Emptying registry.");
            event.getChannel().sendMessage("**Completely emptying the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        if (args[1].equals("rebuild"))
        {
            CommandRegistry.logger.info("Clearing registry.");
            CommandRegistry.registry.clear();
            CommandRegistry.logger.info("Rebuilding registry.");
            CommandInitializer.initCommands();
            CommandRegistry.logger.info("Rebuilt registry.");
            event.getChannel().sendMessage("**Completely rebuilding the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        if (args[1].equals("log"))
        {
            CommandRegistry.logger.info(CommandRegistry.getRegisteredCommandsAndAliases().toString());
            event.getChannel().sendMessage("**Completely logging the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        else
            {
            event.getChannel().sendMessage("Please provide a valid method. `clear` | `rebuild` | `log`").queue();
            CommandRegistry.logger.info("Failed to apply any actions to the registry.");
            System.out.println(args[1]); System.out.println(args[1]);
        }
    }
}