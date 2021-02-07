/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.modules;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.data.Constants;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.main.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventModule extends Module
{
    public static JDA jda;
    public static final Logger log = LoggerFactory.getLogger(EventModule.class);

    @Override
    public void onReady(ReadyEvent event)
    {
        Launcher.onInit(event);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        this.send("Joined guild %s.", event.getGuild().getName());

        try
        {
            String defaultMessage = Config.INS.getJoin();
            defaultMessage = defaultMessage.replace("{prefix}", Constants.GUILD_PREFIX);
            defaultMessage = defaultMessage.replace("{guildName}", event.getGuild().getName());
            TextChannel systemChannel = event.getGuild().getDefaultChannel();
            if (systemChannel == null) {
                event.getGuild().getDefaultChannel().sendMessage(defaultMessage).queue();
                return;
            }
            systemChannel.sendMessage(defaultMessage).queue();
        } catch (PermissionException e)
        {
            log.info("Unable to send welcome message. Insufficient permissions.");
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {
        send("Left guild %s!", event.getGuild().getName());
    }

    private void send(String message, Object options)
    {
        String msg = String.format(message, options);
        log.info(msg);
    }
}