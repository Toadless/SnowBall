package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

public class ReviveCommand extends Command
{
    public ReviveCommand()
    {
        super("revive", null);
        addFlag(CommandFlags.DEVELOPER_ONLY);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("Please provide a shard id to revive.").queue();
            return;
        }
        int shardId = Integer.parseInt(ctx.getArgs()[1]);
        ctx.getChannel().sendMessage("Reviving shard" + " " + shardId).queue();
        Launcher.getInstance(shardId).revive();
    }
}