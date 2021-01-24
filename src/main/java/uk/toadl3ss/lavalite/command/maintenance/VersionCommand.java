package uk.toadl3ss.lavalite.command.maintenance;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

public class VersionCommand extends Command
{
    public VersionCommand()
    {
        super("version", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        event.getChannel().sendMessage("v" + Launcher.version).queue();
    }
}