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

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.audio.GuildMusicManager;
import net.toaddev.lavalite.entities.exception.MusicException;
import net.toaddev.lavalite.entities.module.Module;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.util.DiscordUtil;
import net.toaddev.lavalite.util.FormatTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicModule extends Module
{
    private Map<Long, GuildMusicManager> musicPlayers;
    private AudioPlayerManager audioPlayerManager;

    @Override
    public void onEnable()
    {
        this.musicPlayers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event)
    {
        if (!event.getMember().equals(event.getGuild().getSelfMember()))
        {
            return;
        }
        getInstance().getMusicManager(event.getGuild()).audioPlayer.destroy();
    }

    public GuildMusicManager getMusicManager(Guild guild)
    {
        return this.musicPlayers.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager, guild);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    /**
     *
     * @param channel The channel that the message took place in
     * @param trackUrl The track url that has been provided
     * @param event The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     */
    public void loadAndPlay(TextChannel channel, String trackUrl, GuildMessageReceivedEvent event)
    {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                musicManager.scheduler.queue(track);
                sendAddedEmbed(track, channel, event);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                final List<AudioTrack> tracks = playlist.getTracks();
                if (playlist.isSearchResult())
                {
                    sendAddedEmbed(tracks.get(0), channel, event);
                    musicManager.scheduler.queue(tracks.get(0));
                }
                else
                {
                    channel.sendMessage("Adding to queue: `")
                            .append(String.valueOf(tracks.size()))
                            .append("` tracks from playlist `")
                            .append(playlist.getName())
                            .append("`")
                            .queue();
                    musicManager.scheduler.queue(tracks.get(0));
                    for (final AudioTrack track: tracks)
                    {
                        musicManager.scheduler.queue(track);
                    }
                }
            }

            @Override
            public void noMatches()
            {
                channel.sendMessage(":x: No songs found matching `" + event.getMessage().getContentRaw().replace(Launcher.getModules().get(DatabaseModule.class).getPrefix(channel.getGuild().getIdLong()) + "play", "") + "`").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                channel.sendMessage(":x: Failed to load the provided song. Please try again").queue();
                throw new MusicException("Failed to load a song");
            }
        });
    }

    /**
     *
     * @param track The audio track
     * @param channel The text channel to send the embed to
     * @param event The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     */
    public static void sendAddedEmbed(AudioTrack track, TextChannel channel, GuildMessageReceivedEvent event)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Added to queue", track.getInfo().uri, event.getAuthor().getAvatarUrl());
        embed.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")");
        embed.addField("**Channel**", track.getInfo().author, true);
        embed.addField("**Song Duration**", FormatTimeUtil.formatTime(track.getDuration()), true);
        embed.setColor(DiscordUtil.getEmbedColor());
        channel.sendMessage(embed.build()).queue();
    }

    public static MusicModule getInstance()
    {
        return Launcher.getMusicModule();
    }

    @Override
    public void onDisable()
    {
        this.musicPlayers.clear();
    }
}