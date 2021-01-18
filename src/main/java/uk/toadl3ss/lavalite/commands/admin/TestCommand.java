package uk.toadl3ss.lavalite.commands.admin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.CommandManager;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandOwnerRestricted;

public class TestCommand extends Command implements ICommandOwnerRestricted {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        CommandManager.logger.info("Test command executed.");
        event.getChannel().sendMessage("Test").queue();
    }
}