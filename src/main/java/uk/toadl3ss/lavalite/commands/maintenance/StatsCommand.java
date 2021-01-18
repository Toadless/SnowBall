package uk.toadl3ss.lavalite.commands.maintenance;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Lavalite;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class StatsCommand extends Command implements ICommandMaintenance {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        long totalSecs = (System.currentTimeMillis() - Lavalite.START_TIME) / 1000;

        String str;

        str = "\n\n```java\n";

        str = str + "Reserved memory:                " + Runtime.getRuntime().totalMemory() / 1000000 + "MB\n";
        str = str + "-> Of which is used:            " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000 + "MB\n";
        str = str + "-> Of which is free:            " + Runtime.getRuntime().freeMemory() / 1000000 + "MB\n";
        str = str + "Max reservable:                 " + Runtime.getRuntime().maxMemory() / 1000000 + "MB\n";

        str = str + "\n----------\n\n";

        str = str + "Shards:                         " + Lavalite.getAllShards() + "\n";

        str = str + "Known servers:                  " + Lavalite.getAllGuilds() + "\n";
        str = str + "Known users in servers:         " + Lavalite.getAllUsersAsMap() + "\n";
        str = str + "JDA responses total:            " + event.getGuild().getJDA().getResponseTotal() + "\n";
        str = str + "JDA version:                    " + JDAInfo.VERSION;

        str = str + "\n----------\n\n";

        str = str + "Start Time                      " + totalSecs + " " + "seconds ago" + "\n";

        str = str + "```";

        event.getChannel().sendMessage(str).queue();
    }
}