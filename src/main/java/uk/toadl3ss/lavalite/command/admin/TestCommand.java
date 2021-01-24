package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class TestCommand extends Command {
    public TestCommand()
    {
        super("test", null, PermissionLevel.BOT_ADMIN);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        System.out.println("Test command executed");
        event.getChannel().sendMessage("Test command indeed").queue();
    }
}