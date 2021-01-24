package uk.toadl3ss.lavalite.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandFlags;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.ICommandMusic;
import uk.toadl3ss.lavalite.util.FormatTime;

public class InfoCommand extends Command implements ICommandMusic
{
    public InfoCommand()
    {
        super("info", "Displays info about the guilds player");
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        final TextChannel channel = (TextChannel) event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel to display the guilds play information.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if (audioPlayer.getPlayingTrack() == null)
        {
            channel.sendMessage("I have no current song playing. No info to show.").queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```md\n");
        stringBuilder.append("< ");
        stringBuilder.append(event.getGuild().getName());
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