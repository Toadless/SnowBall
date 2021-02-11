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

package net.toaddev.snowball.modules;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.toaddev.snowball.entities.music.MusicManager;
import net.toaddev.snowball.entities.command.CommandContext;
import net.toaddev.snowball.entities.exception.MusicException;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.entities.music.SearchProvider;
import net.toaddev.snowball.main.Launcher;
import net.toaddev.snowball.util.DiscordUtil;
import net.toaddev.snowball.util.MessageUtils;
import net.toaddev.snowball.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MusicModule extends Module
{
    private Map<Long, MusicManager> musicPlayers;
    private AudioPlayerManager audioPlayerManager;


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

        this.urlPattern = Pattern.compile("^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$");
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event)
    {
        try
        {
            if(event.getUser().isBot())
            {
                return;
            }
            var manager = this.musicPlayers.get(event.getGuild().getIdLong());

            if(manager == null)
            {
                return;
            }

            var scheduler = manager.getScheduler();
            var member = event.getMember();

            var voiceState = member.getVoiceState();

            if(voiceState == null || voiceState.getChannel() == null)
            {
                return;
            }

            var messageId = event.getMessageIdLong();

            if(messageId != getMusicManager(event.getGuild()).getMusicController()){
                return;
            }

            switch (event.getReactionEmote().getAsReactionCode())
            {
                case "\u27A1\uFE0F" -> scheduler.nextTrack();
                case "\u23ef\ufe0f" -> scheduler.player.setPaused(!scheduler.player.isPaused());
                case "\uD83D\uDD00" -> scheduler.shuffle();
                case "\uD83D\uDD09" -> scheduler.player.setVolume(scheduler.player.getVolume() - 50);
                case "\uD83D\uDD0A" -> scheduler.player.setVolume(scheduler.player.getVolume() + 50);
                case "\u274C" -> destroy(event.getGuild().getIdLong(), event.getMember().getIdLong(), true);
            }
            if(event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE)){
                event.getReaction().removeReaction(event.getUser()).queue();
            }
        }
        catch (Exception e)
        {
            event.getChannel().sendMessage("Your request could not be completed!").queue();
        }
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event){
        this.musicPlayers.remove(event.getGuild().getIdLong());
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

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event)
    {
        if(event instanceof GuildVoiceLeaveEvent || event instanceof GuildVoiceMoveEvent)
        {
            var manager = getMusicManager(event.getEntity().getGuild());
            if(manager == null){
                return;
            }
            if(event.getEntity().getIdLong() == Launcher.getJda().getSelfUser().getIdLong())
            {
                this.modules.get(MusicModule.class).destroy(manager, -1L, false);
                return;
            }
            var channel = event.getChannelLeft();
            var scheduler = manager.getScheduler();
            var currentChannel = scheduler.guild.getAudioManager().getConnectedChannel();
            if (currentChannel == null)
            {
                return;
            }
            var currentChannelId = currentChannel.getIdLong();
            if(channel == null || channel.getIdLong() != currentChannelId){
                return;
            }
            if(channel.getMembers().stream().anyMatch(member -> !member.getUser().isBot())){
                return;
            }
            manager.planDestroy();
        }
        else if(event instanceof GuildVoiceJoinEvent){
            var player = getMusicManager(event.getEntity().getGuild());
            if(player == null){
                return;
            }
            player.cancelDestroy();
        }
    }


    public void destroy(long guildId, long userId, boolean message)
    {
        try
        {
            destroy(this.musicPlayers.get(guildId), userId, message);
        }
        catch (Exception ignored)
        {

        }
    }

    public void destroy(MusicManager musicManager, long userId, boolean msg)
    {
        var scheduler = musicManager.getScheduler();

        this.musicPlayers.remove(musicManager.getScheduler().guild.getIdLong());
        scheduler.player.stopTrack();
        scheduler.player.destroy();
        AudioManager audioManager = scheduler.guild.getAudioManager();
        audioManager.closeAudioConnection();
        musicManager.removeMusicController();

        if (!msg)
        {
            return;
        }

        var channel = Launcher.getModules().get(MessageModule.class).getLatestMessage().get(scheduler.guild.getIdLong()).getTextChannel();
        if(channel == null || !channel.canTalk())
        {
            return;
        }
        var message = userId == -1 ? "Disconnected due to inactivity" : MessageUtils.getUserMention(userId) + " disconnected me";
        channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription(message).setTimestamp(Instant.now()).build()).queue();
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

    /**
     *
     * @param ctx The {@link net.toaddev.snowball.entities.command.CommandContext context} to use.
     * @param query The query to play.
     * @param searchProvider The {@link net.toaddev.snowball.entities.music.SearchProvider searchProvider} to use.
     * @param messages Are we going to send the added to the queue messages or not?
     * @param search This is for the dynamic search page.
     */
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
                case YOUTUBE -> query = "ytsearch:" + query;
                case SOUNDCLOUD -> query = "scsearch:" + query;
            }
        }

        try
        {
            if (!search)
            {
                manager.getScheduler().loadItem(query, manager, ctx, messages);
            } else
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
    }
}