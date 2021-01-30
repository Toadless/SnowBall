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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.entities.command.CommandManager;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.database.GuildRegistry;

public class EventListenerLite extends AbstractEventListener
{
    // ################################################################################
    // ##                     Message Received Event
    // ################################################################################
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";

        String[] args = event.getMessage().getContentRaw().split(" ");

        long guildId = Long.parseLong(event.getGuild().getId());

        if (event.getMessage().isWebhookMessage())
        {
            return;
        }
        else if (event.getAuthor().equals(event.getJDA().getSelfUser()))
        {
            return;
        }
        else if (event.getMember().getUser().isBot())
        {
            return;
        }
        else if (Launcher.DATABASE_ENABLED)
        {
            String guildPrefix = GuildRegistry.getPrefix(guildId);
            if (event.getMessage().getContentRaw().startsWith(guildPrefix))
            {
                this.executeCommand(args, event, guildPrefix);
                return;
            }
            else if (event.getMessage().getContentRaw().startsWith(mention))
            {
                this.executeCommand(args, event, guildPrefix);
                return;
            }
            return;
        }
        else if (event.getMessage().getContentRaw().startsWith(Constants.GUILD_PREFIX))
        {
            this.executeCommand(args, event, Constants.GUILD_PREFIX);
            return;
        }
        else if (event.getMessage().getContentRaw().startsWith(mention))
        {
            this.executeCommand(args, event, Constants.GUILD_PREFIX);
            return;
        }
        return;
    }

    public void executeCommand(String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        String[] a = args;
        GuildMessageReceivedEvent e = event;
        String p = prefix;
        CommandManager manager = Launcher.getCommandManager();
        manager.handleCommand(a, e, p);
    }
}