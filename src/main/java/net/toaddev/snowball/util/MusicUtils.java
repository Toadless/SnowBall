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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.modules.PaginatorModule;
import net.toaddev.snowball.services.Modules;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

public class MusicUtils
{
    private MusicUtils()
    {
        // override, default, constructor
    }

    public static String formatTrackWithInfo(AudioTrack track)
    {
        var info = track.getInfo();
        return formatTrack(track) + " - " + TimeUtils.formatDuration(info.length);
    }

    public static String formatTrack(AudioTrack track)
    {
        var info = track.getInfo();
        return MessageUtils.maskLink("`" + info.title + "`", info.uri);
    }

    public static void sendAddedEmbed(CommandContext ctx, int trackCount)
    {
        ctx.sendMessage(new EmbedBuilder()
                .setDescription("Use `" + ctx.getPrefix() + "queue` to view the queue")
                .setAuthor("Queued " + trackCount + " tracks")
                .setTimestamp(Instant.now())
                .setColor(DiscordUtil.getEmbedColor())
                .build());
    }

    public static void sendTracks(Collection<AudioTrack> tracks, Modules modules, TextChannel channel, long authorId, String baseMessage)
    {
        if (channel == null)
        {
            return;
        }
        var trackMessage = new StringBuilder("**").append(baseMessage).append(":**\n");
        var pages = new ArrayList<String>();

        var i = 1;
        for (var track : tracks)
        {
            var formattedTrack = i + ". " + MusicUtils.formatTrackWithInfo(track) + "\n";
            if (trackMessage.length() + formattedTrack.length() >= 2048)
            {
                pages.add(trackMessage.toString());
                trackMessage = new StringBuilder();
            }
            trackMessage.append(formattedTrack);
            i++;
        }
        pages.add(trackMessage.toString());

        modules.get(PaginatorModule.class).create(
                channel,
                authorId,
                pages.size(),
                (page, embedBuilder) -> embedBuilder.setColor(DiscordUtil.getEmbedColor())
                        .setDescription(pages.get(page))
                        .setTimestamp(Instant.now())
        );
    }
}
