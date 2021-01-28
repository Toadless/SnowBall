package net.toaddev.lavalite.audio;

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
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.database.GuildRegistry;
import net.toaddev.lavalite.entities.exception.MusicException;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.util.DiscordUtil;
import net.toaddev.lavalite.util.FormatTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager
{
    // ################################################################################
    // ##                     Player Manager
    // ################################################################################
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager()
    {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild)
    {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
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
                { // Adding a single song from search result
                    sendAddedEmbed(tracks.get(0), channel, event);
                    musicManager.scheduler.queue(tracks.get(0));
                }
                else
                    { // Adding a whole playlist
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
                return;
            }

            @Override
            public void noMatches()
            {
                if (Launcher.DATABASE_ENABLED)
                {
                    channel.sendMessage(":x: No songs found matching `" + event.getMessage().getContentRaw().replace(GuildRegistry.getPrefix(Long.parseLong(event.getGuild().getId())) + "play", "") + "`").queue();
                    return;
                }
                channel.sendMessage(":x: No songs found matching `" + event.getMessage().getContentRaw().replace(Constants.GUILD_PREFIX + "play", "") + "`").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                channel.sendMessage(":x: Failed to load the provided song. Please try again").queue();
                throw new MusicException("Failed to load a song");
            }
        });
    }

    public static PlayerManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
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
        embed.addField("**Song Duration**", FormatTime.formatTime(track.getDuration()), true);
        embed.setColor(DiscordUtil.getEmbedColor());
        channel.sendMessage(embed.build()).queue();
    }
}