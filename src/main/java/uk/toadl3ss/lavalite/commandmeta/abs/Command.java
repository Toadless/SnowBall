package uk.toadl3ss.lavalite.commandmeta.abs;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public abstract class Command implements ICommand {
    public void onInvoke(ArrayList<String> args, MessageReceivedEvent event, String prefix){
        String[] newA = new String[args.size()];
        int i = 0;
        for (String str : args) {
            newA[i] = str;
            i++;
        }
        onInvoke(newA, event, prefix);
    }
}