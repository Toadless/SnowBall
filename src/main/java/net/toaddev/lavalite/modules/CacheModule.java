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

package net.toaddev.lavalite.modules;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.entities.modules.Module;

import java.util.HashMap;
import java.util.Map;


public class CacheModule extends Module
{
    private Map<Long, Message> latestMessage = new HashMap<>();
    public CacheModule()
    {
        super("cache");
    }

    @Override
    public void onEnable()
    {
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if(event.getMessage().getContentRaw().isBlank())
        {
            return;
        }
        cacheMessage(event.getMessage());
    }

    private void cacheMessage(Message message){
        try
        {
            this.latestMessage.remove(message.getGuild().getIdLong());
        }
        catch (NullPointerException e)
        {
        }
        this.latestMessage.put(message.getGuild().getIdLong(), message);
    }

    public Map<Long, Message> getLatestMessage()
    {
        return latestMessage;
    }

    @Override
    public void onDisable()
    {
        this.latestMessage.clear();
    }
}