/*
 * MIT License
 *
 * Copyright (c) 2021 Toadless @ toaddev.net
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

package net.toaddev.lavalite.entities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.entities.exception.MusicException;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.modules.DatabaseModule;
import net.toaddev.lavalite.modules.MusicModule;
import net.toaddev.lavalite.util.MusicUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.toaddev.lavalite.modules.MusicModule.sendAddedEmbed;

public class AudioLoader implements AudioLoadResultHandler
{
    private final MusicManager musicManager;

    private final TextChannel channel;
    private final GuildMessageReceivedEvent event;

    private final boolean messages;
    private final CommandContext ctx;

    public AudioLoader(CommandContext ctx, MusicModule musicModule, boolean messages)
    {
        this.musicManager = musicModule.getMusicManager(ctx.getGuild());

        this.channel = ctx.getChannel();
        this.event = ctx.getEvent();

        this.messages = messages;
        this.ctx = ctx;
    }

    @Override
    public void trackLoaded(AudioTrack track)
    {
        musicManager.getScheduler().queue(track);

        if (messages)
        {
            sendAddedEmbed(track, channel, event);
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist)
    {
        final List<AudioTrack> tracks = playlist.getTracks();
        if (playlist.isSearchResult())
        {
            musicManager.getScheduler().queue(tracks.get(0));

            if (messages)
            {
                sendAddedEmbed(tracks.get(0), channel, event);
            }
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
        if (!ctx.getArgs()[1].contains("spotify."))
            channel.sendMessage(":x: No songs found matching `" + event.getMessage().getContentRaw().replace(Launcher.getModules().get(DatabaseModule.class).getPrefix(channel.getGuild().getIdLong()) + "play", "") + "`").queue();
    }

    @Override
    public void loadFailed(FriendlyException exception)
    {
        channel.sendMessage(":x: Failed to load the provided song. Please try again").queue();
        throw new MusicException("Failed to load a song");
    }

    public static class AudioLoaderList implements AudioLoadResultHandler
    {
        private final MusicManager musicManager;

        private final TextChannel channel;
        private final GuildMessageReceivedEvent event;
        private final User user;

        public AudioLoaderList(CommandContext ctx, MusicModule musicModule)
        {
            this.musicManager = musicModule.getMusicManager(ctx.getGuild());

            this.channel = ctx.getChannel();
            this.event = ctx.getEvent();
            this.user = ctx.getMember().getUser();
        }

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
                    MusicUtils.sendTracks(tracks, Launcher.getModules(), channel, user.getIdLong(), "Found " + tracks.size() + " tracks:");

                    Launcher.getEventWaiter().waitForEvent(
                            GuildMessageReceivedEvent.class,
                            (e) -> e.getMember().getIdLong() == user.getIdLong() && e.getGuild().getIdLong() == channel.getGuild().getIdLong() && e.getChannel().getIdLong() == channel.getIdLong(),
                            (e) ->
                            {
                                String[] args = e.getMessage().getContentRaw().split("\\s+");
                                try
                                {
                                    int num = Integer.parseInt(args[0]);

                                    num = (num - 1);

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
    }
}