package uk.toadl3ss.lavalite.commands.admin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandOwnerRestricted;
import uk.toadl3ss.lavalite.main.Lavalite;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.utils.ExitCodes;

public class ExitCommand extends Command implements ICommandOwnerRestricted {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        event.getChannel().sendMessage("Exiting...").complete();
        Lavalite.shutdown(ExitCodes.EXIT_CODE_NORMAL);
    }
}