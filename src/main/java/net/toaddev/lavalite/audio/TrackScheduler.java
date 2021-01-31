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

package net.toaddev.lavalite.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.toaddev.lavalite.entities.exception.MusicException;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.modules.MessageModule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackScheduler extends AudioEventAdapter
{
    // ################################################################################
    // ##                     Track Scheduler
    // ################################################################################
    public final AudioPlayer player;
    public final Queue<AudioTrack> queue;
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
        try {
            AudioTrack nextTrack = this.queue.poll();
            if (nextTrack == null)
            {
                return;
            }
            this.player.startTrack(nextTrack, false);

            Message latestMessage = Launcher.getModules().get(MessageModule.class).getLatestMessage().get(guild.getIdLong());
            if (latestMessage == null)
            {
                return;
            }
            latestMessage.getChannel().sendMessage("Now playing: `" + nextTrack.getInfo().title + "`!").queue();
        } catch (Exception e) {
            throw new MusicException("Error skipping to next track.");
        }
    }

    public void shuffle()
    {
        Collections.shuffle((List<?>) queue);
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
}