package uk.toadl3ss.lavalite.command.fun;

import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

public class JokeCommand extends Command
{
    public JokeCommand()
    {
        super("joke", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {

    }
}
