package net.toaddev.lavalite.command.admin;

import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;
import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;

public class TestCommand extends Command
{
    public TestCommand()
    {
        super("test", null);
        addFlag(CommandFlags.DISABLED);
    }
    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        ctx.getChannel().sendMessage("Test indeed").queue();
    }
}