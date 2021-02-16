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

package net.toaddev.snowball.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.main.BotController;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class EmbedUtil
{
    private EmbedUtil()
    {
        //Overrides the default, public, constructor
    }

    public static void sendError(MessageChannel channel, String errorText)
    {
        sendDeletingEmbed(channel, new EmbedBuilder()
                .setDescription(errorText)
                .setColor(Color.RED)
                .setTimestamp(Instant.now()));
    }

    public static void sendSuccess(MessageChannel channel, String successText)
    {
        sendDeletingEmbed(channel, new EmbedBuilder()
                .setDescription(successText)
                .setColor(Color.GREEN)
                .setTimestamp(Instant.now()));
    }

    public static void sendDisabledError(CommandContext ctx)
    {
        sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
                .setDescription("This command is currently disabled!")
                .setColor(Color.RED)
                .setTimestamp(Instant.now()));
    }

    public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed, long delay)
    {
        try
        {
            channel.sendMessage(embed.build()).queue(message -> message.delete().queueAfter(delay, TimeUnit.MILLISECONDS, null, error ->
            {
            }));
        } catch (PermissionException e)
        {
            try
            {
                channel.sendMessage("I do not have any permissions. My lowest required permissions is `MESSAGE_EMBED_LINKS`.").queue();
            } catch (PermissionException exception)
            {
                Guild guild = BotController.getJda().getGuildChannelById(channel.getId()).getGuild();
                if (guild == null)
                {
                    return;
                }
                guild.leave().queue();
            }
        }
    }

    public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed)
    {
        sendDeletingEmbed(channel, embed, 10000);
    }
}