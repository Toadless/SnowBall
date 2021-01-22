package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandRestricted;
import uk.toadl3ss.lavalite.commandmeta.init.CommandInitializer;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class RegistryCommand extends Command implements ICommandRestricted {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        if (!Config.INS.getDevelopment()) {
            event.getChannel().sendMessage("This command can only be ran in development.").queue();
            return;
        }
        if (args.length < 2) {
            event.getChannel().sendMessage("Please provide an option. `clear` | `rebuild`").queue();
            return;
        }
        if (args[1].equals("clear")) {
            CommandRegistry.registry.clear();
            CommandRegistry.logger.info("Emptying registry.");
            event.getChannel().sendMessage("**Completely emptying the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        if (args[1].equals("rebuild")) {
            CommandRegistry.logger.info("Clearing registry.");
            CommandRegistry.registry.clear();
            CommandRegistry.logger.info("Rebuilding registry.");
            CommandInitializer.initCommands();
            CommandRegistry.logger.info("Rebuilt registry.");
            event.getChannel().sendMessage("**Completely rebuilding the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        else {
            event.getChannel().sendMessage("Please provide a valid method. `clear` | `rebuild`").queue();
            CommandRegistry.logger.info("Failed to apply any actions to the registry.");
            System.out.println(args[1]); System.out.println(args[1]);
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public PermissionLevel getMinimumPerms() {
        return PermissionLevel.BOT_ADMIN;
    }
}