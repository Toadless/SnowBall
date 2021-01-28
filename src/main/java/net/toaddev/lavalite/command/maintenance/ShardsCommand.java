package net.toaddev.lavalite.command.maintenance;

import net.dv8tion.jda.api.utils.MiscUtil;
import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.main.Launcher;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;

public class ShardsCommand extends Command
{
    public ShardsCommand()
    {
        super("shards", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        int sID = MiscUtil.getShardForGuild(ctx.getGuild(), (int) Launcher.getAllShards());
        sID++;
        String str;
        str = "\n\n```java\n";
        str = str + "Total Shards:           " + Launcher.getAllShards() + "\n";
        str = str + "Your Shard ID:          " + sID + "\n";
        str = str + "```";
        ctx.getChannel().sendMessage(str).queue();
    }
}