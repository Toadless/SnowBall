package net.toaddev.lavalite.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.lavalite.audio.GuildMusicManager;
import net.toaddev.lavalite.audio.PlayerManager;
import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.util.DiscordUtil;
import net.toaddev.lavalite.util.FormatTime;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class QueueCommand extends Command
{
    public QueueCommand()
    {
        super("queue", "Displays the guilds queue");
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel())
        {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel for this to work.").queue();
            return;
        }
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if (audioPlayer.getPlayingTrack() == null)
        {
            channel.sendMessage("No current playing song.").queue();
            return;
        }
        final Queue<AudioTrack> queue = musicManager.scheduler.queue;
        if (queue.isEmpty())
        {
            channel.sendMessage("The queue is empty.").queue();
            return;
        }
        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Queue");
        embed.setColor(DiscordUtil.getEmbedColor());
        final AudioTrack currentTrack = audioPlayer.getPlayingTrack();
        final AudioTrackInfo currentTrackInfo = currentTrack.getInfo();
        embed.addField("**Now Playing**", "\u200C", false);
        String currentTrackField = ("by" + " " + currentTrackInfo.author + " `[" + FormatTime.formatTime(currentTrackInfo.length) + "]`");
        embed.addField(currentTrackInfo.title, currentTrackField, false);
        embed.addField("\u200C", "\u200C", false);
        embed.addField("**Up Next:**", "\u200C", false);
        for(int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();
            String field1 = ("`" + String.valueOf(i + 1) + ".`" + " " + info.title);
            String field2 = ("by" + " " + info.author + " `[" + FormatTime.formatTime(track.getDuration()) + "]`");
            embed.addField(field1, field2, false);
        }
        if (trackList.size() > trackCount) {
            embed.addBlankField(false);
            embed.addField("\u200C", "And" + " `" + String.valueOf(trackList.size() - trackCount) + "` " + "more...", false);
        }
        channel.sendMessage(embed.build()).queue();
    }
}