package net.toaddev.lavalite.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.lavalite.audio.GuildMusicManager;
import net.toaddev.lavalite.audio.PlayerManager;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;
import net.toaddev.lavalite.entities.command.CommandFlags;
import net.toaddev.lavalite.entities.command.abs.Command;
import net.toaddev.lavalite.util.FormatTime;

public class InfoCommand extends Command
{
    public InfoCommand()
    {
        super("info", "Displays info about the guilds player");
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

        if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel to display the guilds play information.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if (audioPlayer.getPlayingTrack() == null)
        {
            channel.sendMessage("I have no current song playing. No info to show.").queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```md\n");
        stringBuilder.append("< ");
        stringBuilder.append(ctx.getGuild().getName());
        stringBuilder.append(" Music Info >\n");
        stringBuilder.append("Current Song:\n");
        stringBuilder.append("# ");
        stringBuilder.append(musicManager.audioPlayer.getPlayingTrack().getInfo().title);
        stringBuilder.append("\n");
        stringBuilder.append("Queue size:\n");
        stringBuilder.append("# ");
        stringBuilder.append(musicManager.scheduler.queue.size());
        stringBuilder.append("\n");
        stringBuilder.append("Current Track Position:\n");
        stringBuilder.append("# ");
        stringBuilder.append(FormatTime.formatTime(musicManager.audioPlayer.getPlayingTrack().getPosition()));
        stringBuilder.append("\n");
        stringBuilder.append("Duration Left:\n");
        stringBuilder.append("# ");
        stringBuilder.append(FormatTime.formatTime(musicManager.audioPlayer.getPlayingTrack().getDuration() - musicManager.audioPlayer.getPlayingTrack().getPosition()));
        stringBuilder.append("\n");
        stringBuilder.append("Looping:\n");
        stringBuilder.append("# ");
        stringBuilder.append(musicManager.scheduler.repeating);
        stringBuilder.append("\n");
        stringBuilder.append("Volume:\n");
        stringBuilder.append("# ");
        stringBuilder.append(musicManager.audioPlayer.getVolume());
        stringBuilder.append("\n");
        stringBuilder.append("Paused:\n");
        stringBuilder.append("# ");
        stringBuilder.append(musicManager.scheduler.player.isPaused());
        stringBuilder.append("\n");
        stringBuilder.append("```");
        channel.sendMessage(stringBuilder.toString()).queue();
    }
}