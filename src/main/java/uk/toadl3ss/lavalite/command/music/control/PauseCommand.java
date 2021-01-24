package uk.toadl3ss.lavalite.command.music.control;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMusic;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class PauseCommand extends Command implements ICommandMusic {
    public PauseCommand()
    {
        super("pause", "Pauses the guilds player", PermissionLevel.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix) {
        final TextChannel channel = (TextChannel) event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I need to be in a voice channel for this to work.").queue();
            return;
        }
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if (audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("No current playing song.").queue();
            return;
        }
        boolean paused = musicManager.scheduler.player.isPaused();
        musicManager.scheduler.player.setPaused(!paused);
        String status = paused ? "paused" : "playing";
        String newStatus = !paused ? "paused" : "playing";
        channel.sendMessage("Changed the player from **" + status+ "** to **" + newStatus + "**.").queue();
    }
}