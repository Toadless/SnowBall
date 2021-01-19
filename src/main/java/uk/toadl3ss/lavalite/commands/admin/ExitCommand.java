package uk.toadl3ss.lavalite.commands.admin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandOwnerRestricted;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.utils.ExitCodes;

public class ExitCommand extends Command implements ICommandOwnerRestricted {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        event.getChannel().sendMessage("Exiting...").complete();
        Launcher.shutdown(ExitCodes.EXIT_CODE_NORMAL);
    }
}