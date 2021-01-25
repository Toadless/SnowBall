package uk.toadl3ss.lavalite.command.maintenance;

import net.dv8tion.jda.api.utils.MiscUtil;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

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