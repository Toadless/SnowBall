package uk.toadl3ss.lavalite.commandmeta.abs;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ICommand {
    public abstract void onInvoke(String[] args, MessageReceivedEvent event, String prefix);
}