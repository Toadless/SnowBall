package uk.toadl3ss.lavalite.command.maintenance;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandType;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class StatsCommand extends Command implements ICommandMaintenance
{
    public StatsCommand()
    {
        super("stats", null, PermissionLevel.DEFAULT, CommandType.PRODUCTION);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
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
}