package uk.toadl3ss.lavalite.command.music.control;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.entities.command.abs.ICommandMusic;

public class StopCommand extends Command implements ICommandMusic
{
    public StopCommand()
    {
        super("stop", "Stops the guilds player");
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
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        channel.sendMessage("The player has stopped.").queue();
    }
}