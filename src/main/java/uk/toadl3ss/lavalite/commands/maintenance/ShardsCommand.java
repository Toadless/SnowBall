package uk.toadl3ss.lavalite.commands.maintenance;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.MiscUtil;
import uk.toadl3ss.lavalite.Lavalite;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class ShardsCommand extends Command {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        String str;
        str = "\n\n```java\n";
        str = str + "Total Shards:           " + Lavalite.getAllShards() + "\n";
        str = str + "Your Shard ID:          " + MiscUtil.getShardForGuild(event.getGuild(), Lavalite.getAllShards()) + "\n";
        str = str + "```";
        event.getChannel().sendMessage(str).queue();
    }
}