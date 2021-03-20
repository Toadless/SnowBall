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

package net.toaddev.snowball.objects.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.modules.MessageModule;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.objects.Emoji;
import net.toaddev.snowball.util.DiscordUtil;
import net.toaddev.snowball.util.MusicUtils;
import net.toaddev.snowball.util.TimeUtils;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MusicManager
{
    private final AudioPlayer audioPlayer;
    private final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    private long musicControllerId;
    private ScheduledFuture<?> future;

    public MusicManager(AudioPlayerManager manager, Guild guild)
    {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer, guild);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
        this.future = null;
    }

    public AudioPlayerSendHandler getSendHandler()
    {
        return sendHandler;
    }

    public AudioPlayer getAudioPlayer()
    {
        return audioPlayer;
    }

    public TrackScheduler getScheduler()
    {
        return scheduler;
    }

    public EmbedBuilder buildMusicController()
    {
        try
        {
            var embed = new EmbedBuilder();
            var track = this.scheduler.player.getPlayingTrack();
            embed.setColor(DiscordUtil.getEmbedColor())
                    .addField("Playing", Emoji.FORWARD.get() + " " + MusicUtils.formatTrack(track), false)
                    .addField("Length", TimeUtils.formatDuration(track.getDuration()), true)
                    .addField("Volume", this.scheduler.player.getVolume() + "%", true)
                    .addField("Repeat Mode", this.scheduler.repeating ? "Enabled" : "Disabled", true)
                    .setTimestamp(Instant.now());
            return embed;
        } catch (Exception e)
        {
            return null;
        }
    }

    public void sendMusicController()
    {
        try
        {
            Map<Long, Message> latestMessages = BotController.getModules().get(MessageModule.class).getLatestMessage();
            Message latestMessage = latestMessages.get(this.scheduler.guild.getIdLong());

            if (latestMessage == null)
            {
                return;
            }

            TextChannel channel = latestMessage.getTextChannel();

            if (channel == null || !channel.canTalk())
            {
                return;
            }

            channel.deleteMessageById(this.getMusicController())
                    .queue(null, new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, (e) ->
                    {

                    }));

            var embed = buildMusicController();
            if (!channel.canTalk() && embed != null)
            {
                return;
            }

            channel.sendMessage(embed.build()).queue(message ->
            {
                this.setMusicControllerId(message.getIdLong());
                if (!channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_ADD_REACTION))
                {
                    return;
                }
                message.addReaction(Emoji.VOLUME_DOWN.get()).queue();
                message.addReaction(Emoji.VOLUME_UP.get()).queue();
                message.addReaction(Emoji.ARROW_RIGHT.get()).queue();
                message.addReaction(Emoji.PLAY_PAUSE.get()).queue();
                message.addReaction(Emoji.SHUFFLE.get()).queue();
                message.addReaction(Emoji.X.get()).queue();
            });
        } catch (Exception ignored)
        {
        }
    }

    public long getMusicController()
    {
        return this.musicControllerId;
    }

    public void setMusicControllerId(long id)
    {
        this.musicControllerId = id;
    }

    public void removeMusicController()
    {
        this.musicControllerId = -1L;
    }

    public void planDestroy()
    {
        this.scheduler.player.setPaused(true);
        if (this.future != null)
        {
            return;
        }
        this.future = BotController.getModules().schedule(() -> BotController.getModules().get(MusicModule.class).destroy(this, -1L, true), 2, TimeUnit.MINUTES);
    }

    public void cancelDestroy()
    {
        this.scheduler.player.setPaused(false);
        if (this.future == null)
        {
            return;
        }
        this.future.cancel(true);
        this.future = null;
    }

    public ScheduledFuture<?> getFuture()
    {
        return future;
    }
}