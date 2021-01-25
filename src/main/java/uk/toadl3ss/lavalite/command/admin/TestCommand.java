package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

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