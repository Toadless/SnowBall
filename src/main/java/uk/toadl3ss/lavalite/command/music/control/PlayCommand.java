package uk.toadl3ss.lavalite.command.music.control;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.entities.command.abs.ICommandMusic;
import uk.toadl3ss.lavalite.util.IsUrl;

public class PlayCommand extends Command implements ICommandMusic
{
    public PlayCommand()
    {
        super("play", "Plays the provided song");
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        String songName = ctx.getMessage().getContentRaw().replaceFirst("^" + ctx.getPrefix() + "play" + " ", "");
        songName = songName.replaceFirst("^" + ctx.getPrefix() + "p" + " ", "");
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (ctx.getArgs().length < 2)
        {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
            if (musicManager.audioPlayer.getPlayingTrack() != null)
            {
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

        if (!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        String song = ctx.getArgs()[1];
        if (!IsUrl.isUrl(song))
        {
            if (songName == null)
            {
                channel.sendMessage("Please provide a query or url.").queue();
                return;
            }
            song = "ytsearch:" + songName;
            channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        }
        PlayerManager.getInstance()
                .loadAndPlay(channel, song, ctx.getEvent());
        return;
    }
}