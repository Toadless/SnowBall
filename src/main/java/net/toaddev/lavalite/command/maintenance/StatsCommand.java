package net.toaddev.lavalite.command.maintenance;

import net.dv8tion.jda.api.JDAInfo;
import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.main.Launcher;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;

public class StatsCommand extends Command
{
    public StatsCommand()
    {
        super("stats", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
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
        str = str + "JDA responses total:            " + ctx.getGuild().getJDA().getResponseTotal() + "\n";
        str = str + "JDA version:                    " + JDAInfo.VERSION;

        str = str + "\n----------\n\n";

        str = str + "Start Time                      " + totalSecs + " " + "seconds ago" + "\n";

        str = str + "```";

        ctx.getChannel().sendMessage(str).queue();
    }
}