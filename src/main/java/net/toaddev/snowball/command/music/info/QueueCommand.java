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

package net.toaddev.snowball.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.snowball.entities.music.MusicManager;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.main.Launcher;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.util.MessageUtils;
import net.toaddev.snowball.util.MusicUtils;
import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.entities.command.CommandContext;

import java.util.List;
import java.util.Queue;

@net.toaddev.snowball.annotation.Command
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

        final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
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

        List<AudioTrack> tracks = musicManager.getScheduler().getQueue();

        MusicUtils.sendTracks(tracks, Launcher.getModules(), ctx.getChannel(), ctx.getMember().getUser().getIdLong(), "Currently " + tracks.size() + " " + MessageUtils.pluralize("track", tracks) + " are queued");
    }
}