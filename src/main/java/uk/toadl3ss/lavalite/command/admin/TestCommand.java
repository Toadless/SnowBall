package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandFlags;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;

public class TestCommand extends Command
{
    public TestCommand()
    {
        super("test", null);
        addFlag(CommandFlags.DISABLED);
    }
    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        event.getChannel().sendMessage("Test indeed").queue();
    }
}