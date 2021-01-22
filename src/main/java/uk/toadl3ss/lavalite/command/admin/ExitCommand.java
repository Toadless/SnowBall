package uk.toadl3ss.lavalite.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandRestricted;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.perms.PermissionLevel;
import uk.toadl3ss.lavalite.util.ExitCodes;

public class ExitCommand extends Command implements ICommandRestricted {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        event.getChannel().sendMessage("This will **shut down the whole bot**.").complete();
        Launcher.shutdown(ExitCodes.EXIT_CODE_NORMAL);
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public PermissionLevel getMinimumPerms() {
        return PermissionLevel.BOT_ADMIN;
    }
}