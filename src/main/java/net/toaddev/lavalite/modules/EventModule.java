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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.toaddev.lavalite.audio.PlayerManager;
import net.toaddev.lavalite.data.Config;
import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.module.Module;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventModule extends Module
{
    public static JDA jda;
    public static final Logger log = LoggerFactory.getLogger(EventModule.class);

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

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event)
    {
        PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.destroy();
    }
}