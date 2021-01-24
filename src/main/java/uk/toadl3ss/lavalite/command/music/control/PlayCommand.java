package uk.toadl3ss.lavalite.command.music.control;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMusic;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.perms.PermissionLevel;
import uk.toadl3ss.lavalite.util.isUrl;

public class PlayCommand extends Command implements ICommandMusic {
    public PlayCommand()
    {
        super("play", "Plays the provided song", PermissionLevel.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix) {
        String songName = event.getMessage().getContentRaw().replaceFirst("^" + prefix + "play" + " ", "");
        final TextChannel channel = (TextChannel) event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (args.length < 2) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            if (musicManager.audioPlayer.getPlayingTrack() != null) {
                boolean paused = musicManager.scheduler.player.isPaused();
                musicManager.scheduler.player.setPaused(!paused);
                String status = paused ? "paused" : "playing";
                String newStatus = !paused ? "paused" : "playing";
                channel.sendMessage("Changed the player from **" + status+ "** to **" + newStatus + "**. \n This event occured because a song is and no arguments were provided!").queue();
                return;
            }
            channel.sendMessage("Please provide a url or search query.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()) {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        String song = args[1];
        if (!isUrl.isUrl(song)) {
            if (songName == null) {
                channel.sendMessage("Please provide a query or url.").queue();
                return;
            }
            song = "ytsearch:" + songName;
            channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        }
        PlayerManager.getInstance()
                .loadAndPlay(channel, song, event);
        return;
    }
}