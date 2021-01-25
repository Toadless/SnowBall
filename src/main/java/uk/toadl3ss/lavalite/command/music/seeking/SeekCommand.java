package uk.toadl3ss.lavalite.command.music.seeking;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandManager;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.entities.exception.CommandErrorException;
import uk.toadl3ss.lavalite.util.FormatTime;

public class SeekCommand extends Command
{
    public SeekCommand()
    {
        super("seek", "Seeks to a provided position in the track");
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
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("Please provide a position to seek to.").queue();
            return;
        }
        try
        {
            if (musicManager.scheduler.player.getPlayingTrack().getDuration() < Long.parseLong(ctx.getArgs()[1]))
            {
                ctx.getChannel().sendMessage("The tracks duration is less than: `" + FormatTime.formatTime(Long.parseLong(ctx.getArgs()[1])) + "`.").queue();
                return;
            }
            musicManager.scheduler.player.getPlayingTrack().setPosition(Long.parseLong(ctx.getArgs()[1]));
            ctx.getChannel().sendMessage("The tracks position has been set to: `" + FormatTime.formatTime(Long.parseLong(ctx.getArgs()[1])) + "`.").queue();
        } catch (NumberFormatException e)
        {
            ctx.getChannel().sendMessage("Please provide a valid number.").queue();
            CommandManager.logger.info("NumberFormatException has been thrown");
            throw new CommandErrorException("NumberFormatException has been thrown");
        }
    }
}