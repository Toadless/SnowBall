package uk.toadl3ss.lavalite.commands.maintenance;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MiscUtil;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class ShardsCommand extends Command implements ICommandMaintenance {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
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