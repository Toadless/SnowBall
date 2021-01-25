package uk.toadl3ss.lavalite.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import uk.toadl3ss.lavalite.entities.exception.MusicException;

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
    public boolean repeating = false;

    public TrackScheduler(AudioPlayer player)
    {
        this.player = player;
        this.queue = new LinkedList<>();
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
            this.player.startTrack(this.queue.poll(), false);
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