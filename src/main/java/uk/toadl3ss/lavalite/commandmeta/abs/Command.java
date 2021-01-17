package uk.toadl3ss.lavalite.commandmeta.abs;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command implements ICommand {
    public void onInvoke(String[] args, MessageReceivedEvent event, String prefix, String cmd){
        onInvoke(args, event, prefix);
    }
}