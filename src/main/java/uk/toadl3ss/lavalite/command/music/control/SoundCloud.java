package uk.toadl3ss.lavalite.command.music.control;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.audio.PlayerManager;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandFlags;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;

public class SoundCloud extends Command
{
    public SoundCloud() {
        super("soundcloud", "Plays music from soundcloud");
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        String songName = event.getMessage().getContentRaw().replaceFirst("^" + prefix + "soundcloud" + " ", "");
        songName = songName.replaceFirst("^" + prefix + "sc" + " ", "");
        final TextChannel channel = (TextChannel) event.getChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (args.length < 2)
        {
            channel.sendMessage("Please provide a search query.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        String song = "scsearch:" + songName;
        channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        PlayerManager.getInstance()
                .loadAndPlay(channel, song, event);
        return;
    }
}