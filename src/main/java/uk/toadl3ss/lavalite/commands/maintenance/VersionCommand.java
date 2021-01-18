package uk.toadl3ss.lavalite.commands.maintenance;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMaintenance;
import uk.toadl3ss.lavalite.main.Launcher;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class VersionCommand extends Command implements ICommandMaintenance {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        event.getChannel().sendMessage("v" + Launcher.version).queue();
    }
}