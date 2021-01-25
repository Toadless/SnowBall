package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.entities.database.GuildRegistry;
import uk.toadl3ss.lavalite.main.Launcher;

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