package uk.toadl3ss.lavalite.command.maintenance;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class StatsCommand extends Command implements ICommandMaintenance {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        long totalSecs = (System.currentTimeMillis() - Launcher.START_TIME) / 1000;

        String str;

        str = "\n\n```java\n";

        str = str + "Reserved memory:                " + Runtime.getRuntime().totalMemory() / 1000000 + "MB\n";
        str = str + "-> Of which is used:            " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000 + "MB\n";
        str = str + "-> Of which is free:            " + Runtime.getRuntime().freeMemory() / 1000000 + "MB\n";
        str = str + "Max reservable:                 " + Runtime.getRuntime().maxMemory() / 1000000 + "MB\n";

        str = str + "\n----------\n\n";

        str = str + "Shards:                         " + Launcher.getAllShards() + "\n";

        str = str + "Known servers:                  " + Launcher.getAllGuilds().size() + "\n";
        str = str + "Known users in servers:         " + Launcher.getAllUsersAsMap().size() + "\n";
        str = str + "JDA responses total:            " + event.getGuild().getJDA().getResponseTotal() + "\n";
        str = str + "JDA version:                    " + JDAInfo.VERSION;

        str = str + "\n----------\n\n";

        str = str + "Start Time                      " + totalSecs + " " + "seconds ago" + "\n";

        str = str + "```";

        event.getChannel().sendMessage(str).queue();
    }

    @Override
    public String getHelp() {
        return null;
    }
}