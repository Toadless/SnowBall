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

package net.toaddev.snowball.entities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.toaddev.snowball.entities.command.CommandContext;
import net.toaddev.snowball.entities.exception.MusicException;
import net.toaddev.snowball.main.Launcher;
import net.toaddev.snowball.modules.MessageModule;

import java.util.Collections;
import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter
{
    public final AudioPlayer player;
    private final LinkedList<AudioTrack> queue;
    public final Guild guild;
    public boolean repeating = false;

    public TrackScheduler(AudioPlayer player, Guild guild)
    {
        this.player = player;
        this.queue = new LinkedList<>();
        this.guild = guild;
    }

    public void queue(AudioTrack track)
    {
        if(!this.player.startTrack(track, true))
        {
            this.queue.offer(track);
        }
    }

    public void nextTrack()
    {
        try
        {
            AudioTrack nextTrack = this.queue.poll();
            if (nextTrack == null)
            {
                Launcher.getMusicModule().getMusicManager(guild).planDestroy();
                return;
            }

            Launcher.getMusicModule().getMusicManager(guild).cancelDestroy();

            this.player.startTrack(nextTrack, false);

            Message latestMessage = Launcher.getModules().get(MessageModule.class).getLatestMessage().get(guild.getIdLong());
            if (latestMessage == null)
            {
                return;
            }
            Launcher.getMusicModule().getMusicManager(latestMessage.getGuild()).sendMusicController();
        } catch (Exception e)
        {
            throw new MusicException("Error skipping to next track.");
        }
    }

    public void shuffle()
    {
        Collections.shuffle(queue);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            if (this.repeating)
            {
                this.player.startTrack(track.makeClone(), false);
                return;
            }
            nextTrack();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {
        if (queue.isEmpty() || !queue.contains(track))
        {
            Launcher.getMusicModule().getMusicManager(guild).sendMusicController();
        }
    }

    public void loadItem(String query,  MusicManager manager, CommandContext ctx, boolean messages)
    {
        Launcher.getMusicModule().getAudioPlayerManager().loadItemOrdered(manager, query, new AudioLoader(ctx, Launcher.getMusicModule(), messages));
    }

    public void loadItemList(String query, MusicManager manager, CommandContext ctx)
    {
        Launcher.getMusicModule().getAudioPlayerManager().loadItemOrdered(manager, query, new AudioLoader.AudioLoaderList(ctx, Launcher.getMusicModule()));
    }

    public LinkedList<AudioTrack> getQueue()
    {
        return queue;
    }
}