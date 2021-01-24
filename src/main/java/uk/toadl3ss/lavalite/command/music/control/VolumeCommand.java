package uk.toadl3ss.lavalite.command.music.control;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.commandmeta.CommandManager;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMusic;
import uk.toadl3ss.lavalite.audio.GuildMusicManager;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.perms.PermissionLevel;

public class VolumeCommand extends Command implements ICommandMusic {
    public VolumeCommand()
    {
        super("setvolume", "Changes the players volume", PermissionLevel.DEFAULT);
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
        if (args.length < 2) {
            channel.sendMessage("You need to provide a volume to set").queue();
            return;
        }
        try {
            int volume = Integer.parseInt(args[1]);
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;
            audioPlayer.setVolume(volume);
            channel.sendMessageFormat("Set the volume to: %s", volume).queue();
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Please provide a valid number.").queue();
            CommandManager.logger.info("NumberFormatException has been thrown");
        }
    }
}