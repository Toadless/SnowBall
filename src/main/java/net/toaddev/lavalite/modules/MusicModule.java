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

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.agent.VoiceChannelCleanupAgent;
import net.toaddev.lavalite.entities.music.AudioLoader;
import net.toaddev.lavalite.entities.music.MusicManager;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.entities.exception.MusicException;
import net.toaddev.lavalite.entities.module.Module;
import net.toaddev.lavalite.entities.music.SearchProvider;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.util.DiscordUtil;
import net.toaddev.lavalite.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MusicModule extends Module
{
    private Map<Long, MusicManager> musicPlayers;
    private AudioPlayerManager audioPlayerManager;

    private VoiceChannelCleanupAgent voiceChannelCleanupAgent;

    private Pattern urlPattern;
    public static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]?");
    public static final Pattern SPOTIFY_URL_PATTERN = Pattern.compile("^(https?://)?(www\\.)?open\\.spotify\\.com/(track|album|playlist)/([a-zA-Z0-9-_]+)(\\?si=[a-zA-Z0-9-_]+)?");

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
     * @return The {@link MusicManager musicManager}.
     */
    public MusicManager getMusicManager(Guild guild)
    {
        return this.musicPlayers.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final MusicManager guildMusicManager = new MusicManager(this.audioPlayerManager, guild);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void play(CommandContext ctx, String query, SearchProvider searchProvider, boolean messages, boolean search)
    {
        var manager = getMusicManager(ctx.getGuild());

        var matcher = SPOTIFY_URL_PATTERN.matcher(query);
        if (matcher.matches())
        {
            this.modules.get(SpotifyModule.class).load(ctx, getMusicManager(ctx.getGuild()), matcher);
            return;
        }

        if (!URL_PATTERN.matcher(query).matches())
        {
            switch (searchProvider)
            {
                case YOUTUBE:
                    query = "ytsearch:" + query;
                    break;
                case SOUNDCLOUD:
                    query = "scsearch:" + query;
                    break;
            }
        }

        try
        {
            if (!search)
            {
                manager.getScheduler().loadItem(query, manager, ctx, messages);
            } else if (search)
            {
                manager.getScheduler().loadItemList(query, manager, ctx);
            }
        }
        catch (Exception e)
        {
            ctx.getChannel().sendMessage("This is embarrassing. A wild exception has occurred. \n Exception: `" + e + "`. \n We are sorry for the inconvenience. If the exception persists please contact support.").queue();
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
        embed.addField("**Song Duration**", TimeUtils.formatDuration(track.getDuration()), true);
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

    public AudioPlayerManager getAudioPlayerManager()
    {
        return audioPlayerManager;
    }

    @Override
    public void onDisable()
    {
        this.musicPlayers.clear();
        voiceChannelCleanupAgent.exit();
    }
}