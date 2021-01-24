package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandType;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;
import uk.toadl3ss.lavalite.util.ExitCodes;

public class ExitCommand extends Command
{
    public ExitCommand()
    {
        super("exit", null, PermissionLevel.BOT_ADMIN, CommandType.PRODUCTION);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        event.getChannel().sendMessage("This will **shut down the whole bot**.").complete();
        Launcher.shutdown(ExitCodes.EXIT_CODE_NORMAL);
    }
}