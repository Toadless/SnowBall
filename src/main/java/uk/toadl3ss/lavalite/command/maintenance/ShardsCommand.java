package uk.toadl3ss.lavalite.command.maintenance;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MiscUtil;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandType;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class ShardsCommand extends Command implements ICommandMaintenance
{
    public ShardsCommand()
    {
        super("shards", null, PermissionLevel.DEFAULT, CommandType.PRODUCTION);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        int sID = MiscUtil.getShardForGuild(event.getGuild(), (int) Launcher.getAllShards());
        sID++;
        String str;
        str = "\n\n```java\n";
        str = str + "Total Shards:           " + Launcher.getAllShards() + "\n";
        str = str + "Your Shard ID:          " + sID + "\n";
        str = str + "```";
        event.getChannel().sendMessage(str).queue();
    }
}