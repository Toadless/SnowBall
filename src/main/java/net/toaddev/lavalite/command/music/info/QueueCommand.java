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

package net.toaddev.lavalite.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.lavalite.audio.GuildMusicManager;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.modules.MusicModule;
import net.toaddev.lavalite.util.DiscordUtil;
import net.toaddev.lavalite.util.FormatTimeUtil;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;

import java.time.Instant;
import java.util.List;
import java.util.Queue;

@net.toaddev.lavalite.annotation.Command
public class QueueCommand extends Command
{
    public QueueCommand()
    {
        super("queue", "Displays the guilds queue");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel())
        {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel for this to work.").queue();
            return;
        }
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }

        final GuildMusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (audioPlayer.getPlayingTrack() == null)
        {
            channel.sendMessage("No current playing song.").queue();
            return;
        }

        final Queue<AudioTrack> queue = musicManager.getScheduler().getQueue();

        if (queue.isEmpty())
        {
            channel.sendMessage("The queue is empty.").queue();
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();

        List<AudioTrack> tracks = musicManager.getScheduler().getQueue();
        final int size = tracks.size();
        final int trackCount = Math.min(size, 20);

        {
            for (int i = 0; i < trackCount; i++)
            {
                final AudioTrack track = tracks.get(i);
                final AudioTrackInfo info = track.getInfo();
                stringBuilder
                        .append(i + 1)
                        .append(". `")
                        .append(info.title)
                        .append("` - [")
                        .append(FormatTimeUtil.formatTime(track.getDuration()))
                        .append("] \n");
            }

            if (size > trackCount)
            {
                stringBuilder.append("And `" + (size - trackCount) + "` more...");
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();

            {
                embedBuilder
                        .setAuthor("Currently " + tracks.size() + " tracks are queued: ")
                        .setDescription(stringBuilder.toString())
                        .setColor(DiscordUtil.getEmbedColor())
                        .setTimestamp(Instant.now());
            }

            channel.sendMessage(embedBuilder.build()).queue();
        }
    }
}