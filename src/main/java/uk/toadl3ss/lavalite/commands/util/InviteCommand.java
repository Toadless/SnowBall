package uk.toadl3ss.lavalite.commands.util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.data.Constants;

public class InviteCommand extends Command {
    @Override
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix) {
        if (!Constants.invite) {
            return;
        }
        String str = "https://discord.com/api/oauth2/authorize?client_id=" + event.getJDA().getSelfUser().getId() + "&permissions=267911025&scope=bot";
        event.getChannel().sendMessageFormat("My invite: %s", str).queue();
    }
}