package net.toaddev.lavalite.command.maintenance;

import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.main.Launcher;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;

public class VersionCommand extends Command
{
    public VersionCommand()
    {
        super("version", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        ctx.getChannel().sendMessage("v" + Launcher.version).queue();
    }
}