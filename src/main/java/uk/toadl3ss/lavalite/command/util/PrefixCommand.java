package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandType;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.entities.database.GuildRegistry;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class PrefixCommand extends Command
{
    public PrefixCommand()
    {
        super("prefix", null, PermissionLevel.SERVER_ADMIN, CommandType.PRODUCTION);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        if (!Launcher.DATABASE_ENABLED)
        {
            return;
        }
        long guildId = Long.parseLong(event.getGuild().getId());
        if (args.length < 2)
        {
            event.getChannel().sendMessageFormat("Your prefix is: `%s`", GuildRegistry.getPrefix(guildId)).queue();
            return;
        }
        GuildRegistry.setPrefix(guildId, args[1]);
        event.getChannel().sendMessageFormat("Set the guilds prefix to: `%s`", args[1]).queue();
    }
}