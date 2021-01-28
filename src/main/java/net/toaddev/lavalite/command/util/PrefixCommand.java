package net.toaddev.lavalite.command.util;

import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.main.Launcher;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;
import net.toaddev.lavalite.entities.database.GuildRegistry;

public class PrefixCommand extends Command
{
    public PrefixCommand()
    {
        super("prefix", null);
        addFlag(CommandFlags.SERVER_ADMIN_ONLY);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        if (!Launcher.DATABASE_ENABLED)
        {
            return;
        }
        long guildId = Long.parseLong(ctx.getGuild().getId());
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessageFormat("Your prefix is: `%s`", GuildRegistry.getPrefix(guildId)).queue();
            return;
        }
        GuildRegistry.setPrefix(guildId, ctx.getArgs()[1]);
        ctx.getChannel().sendMessageFormat("Set the guilds prefix to: `%s`", ctx.getArgs()[1]).queue();
    }
}