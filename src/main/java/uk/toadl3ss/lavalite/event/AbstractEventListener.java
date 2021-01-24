package uk.toadl3ss.lavalite.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.toadl3ss.lavalite.main.Launcher;

import java.util.HashMap;

public abstract class AbstractEventListener extends ListenerAdapter
{
    private final HashMap<String, UserListener> userListener = new HashMap<>();

    AbstractEventListener()
    {

    }

    @Override
    public void onReady(ReadyEvent event)
    {
        Launcher.getInstance(event.getJDA()).onInit(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        UserListener listener = userListener.get(event.getAuthor().getId());
        if (listener != null)
        {
            try
            {
                listener.onGuildMessageReceived(event);
            } catch(Exception ex)
            {
            }
        }
    }

    public void putListener(String id, UserListener listener)
    {
        userListener.put(id, listener);
    }

    public void removeListener(String id)
    {
        userListener.remove(id);
    }
}