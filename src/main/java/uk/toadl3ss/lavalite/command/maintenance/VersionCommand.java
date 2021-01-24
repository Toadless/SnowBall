package uk.toadl3ss.lavalite.command.maintenance;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class VersionCommand extends Command implements ICommandMaintenance {
    public VersionCommand()
    {
        super("version", null, PermissionLevel.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix) {
        event.getChannel().sendMessage("v" + Launcher.version).queue();
    }
}