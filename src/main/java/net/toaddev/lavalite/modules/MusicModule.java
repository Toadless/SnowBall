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
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.toaddev.lavalite.agent.VoiceChannelCleanupAgent;
import net.toaddev.lavalite.audio.GuildMusicManager;
import net.toaddev.lavalite.entities.exception.MusicException;
import net.toaddev.lavalite.entities.module.Module;
import net.toaddev.lavalite.entities.music.SearchProvider;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.util.DiscordUtil;
import net.toaddev.lavalite.util.FormatTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MusicModule extends Module
{
    private Map<Long, GuildMusicManager> musicPlayers;
    private AudioPlayerManager audioPlayerManager;

    private VoiceChannelCleanupAgent voiceChannelCleanupAgent;

    private Pattern urlPattern;

    @Override
    public void onEnable()
    {
        this.musicPlayers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

        voiceChannelCleanupAgent = new VoiceChannelCleanupAgent();
        voiceChannelCleanupAgent.setDaemon(true);
        voiceChannelCleanupAgent.start();

        this.urlPattern = Pattern.compile("^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$");
    }

    /**
     *
     * @param event The event.
     *
     * This is only here to save bandwidth and to stop the player from playing
     *
     */
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event)
    {
        if (!event.getMember().equals(event.getGuild().getSelfMember()))
        {
            return;
        }
        getInstance().getMusicManager(event.getGuild()).getAudioPlayer().destroy();
    }

    /**
     *
     * @param guild The {@link net.dv8tion.jda.api.entities.Guild guild} to fetch the music manager from.
     * @return The {@link GuildMusicManager musicManager}.
     */
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
    public void loadAndPlay(TextChannel channel, String trackUrl, GuildMessageReceivedEvent event, SearchProvider searchProvider)
    {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        final String track;

        track = searchProvider.getSearchPrefix() + trackUrl; // This is how we set the song provider. URL, Youtube, Soundcloud

        try
        {
            this.audioPlayerManager.loadItemOrdered(musicManager, track, new AudioLoadResultHandler()
            {
                @Override
                public void trackLoaded(AudioTrack track)
                {
                    musicManager.getScheduler().queue(track);
                    sendAddedEmbed(track, channel, event);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist)
                {
                    final List<AudioTrack> tracks = playlist.getTracks();
                    if (playlist.isSearchResult())
                    {
                        sendAddedEmbed(tracks.get(0), channel, event);
                        musicManager.getScheduler().queue(tracks.get(0));
                    }
                    else
                    {
                        channel.sendMessage("Adding to queue: `")
                                .append(String.valueOf(tracks.size()))
                                .append("` tracks from playlist `")
                                .append(playlist.getName())
                                .append("`")
                                .queue();

                        for (final AudioTrack track : tracks)
                        {
                            musicManager.getScheduler().queue(track);
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
        catch (Exception e)
        {
            channel.sendMessage("This is embarrassing. A wild exception has occurred. \n Exception: `" + e + "`. \n We are sorry for the inconvenience. If the exception persists please contact support.").queue();
            throw new MusicException(e.toString());
        }
    }




    /**
     *
     * @param channel The channel that the message took place in
     * @param trackUrl The track url that has been provided
     * @param event The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     */
    public void loadAndPlayForList(TextChannel channel, String trackUrl, GuildMessageReceivedEvent event, SearchProvider searchProvider, User user)
    {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        final String track;

        track = searchProvider.getSearchPrefix() + trackUrl; // This is how we set the song provider. URL, Youtube, Soundcloud

        try
        {
            this.audioPlayerManager.loadItemOrdered(musicManager, track, new AudioLoadResultHandler()
            {
                @Override
                public void trackLoaded(AudioTrack track)
                {
                    musicManager.getScheduler().queue(track);
                    sendAddedEmbed(track, channel, event);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist)
                {
                    final List<AudioTrack> tracks = playlist.getTracks();

                    {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setAuthor("Found " + tracks.size() + " tracks: ");

                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            int i = 0;

                            for (AudioTrack trackFromList : tracks)
                            {
                                i++;
                                stringBuilder
                                        .append(i)
                                        .append(". `")
                                        .append(trackFromList.getInfo().title)
                                        .append("` - [")
                                        .append(FormatTimeUtil.formatTime(trackFromList.getDuration()))
                                        .append("] \n");
                            }

                            embedBuilder
                                    .setDescription(stringBuilder.toString())
                                    .setColor(DiscordUtil.getEmbedColor())
                                    .setTimestamp(Instant.now());

                            channel.sendMessage(embedBuilder.build())
                                    .queue(message -> {
                                        Launcher.getEventWaiter().waitForEvent(
                                                GuildMessageReceivedEvent.class,
                                                (e) -> e.getMember().getIdLong() == user.getIdLong(),
                                                (e) ->
                                                {
                                                    String[] args = e.getMessage().getContentRaw().split("\\s+");
                                                    try
                                                    {
                                                       int num = Integer.parseInt(args[0]);

                                                       num = num - 1;

                                                       if (tracks.get(num) == null)
                                                       {
                                                           channel.sendMessage("Invalid number!").queue();
                                                           return;
                                                       }

                                                       try
                                                       {
                                                           sendAddedEmbed(tracks.get(num), channel, event);
                                                           musicManager.getScheduler().queue(tracks.get(num));
                                                       }
                                                       catch (IndexOutOfBoundsException exe)
                                                       {
                                                           channel.sendMessage("Invalid number!").queue();
                                                       }
                                                    }
                                                    catch (NumberFormatException ex)
                                                    {
                                                        channel.sendMessage("Invalid number.").queue();
                                                    }
                                                },
                                                10L, TimeUnit.SECONDS,
                                                () -> channel.sendMessage("You took too long.").queue()
                                        );
                                    });
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
        catch (Exception e)
        {
            channel.sendMessage("This is embarrassing. A wild exception has occurred. \n Exception: `" + e + "`. \n We are sorry for the inconvenience. If the exception persists please contact support.").queue();
            throw new MusicException(e.toString());
        }
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

    public boolean isUrl(String url)
    {
        return urlPattern.matcher(url).matches();
    }

    /**
     *
     * @return The instance of this class
     */
    public static MusicModule getInstance()
    {
        return Launcher.getMusicModule();
    }

    @Override
    public void onDisable()
    {
        this.musicPlayers.clear();
        voiceChannelCleanupAgent.exit();
    }
}