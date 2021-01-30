/*
 *  MIT License
 *
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of Lavalite and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of Lavalite, and to permit persons to whom Lavalite is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Lavalite.
 *
 * LAVALITE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package net.toaddev.lavalite.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.toaddev.lavalite.main.Launcher;

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