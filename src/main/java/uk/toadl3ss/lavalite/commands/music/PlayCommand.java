package uk.toadl3ss.lavalite.commands.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMusic;
import uk.toadl3ss.lavalite.player.PlayerManager;
import uk.toadl3ss.lavalite.utils.isUrl;

public class PlayCommand extends Command implements ICommandMusic {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
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
        }
        PlayerManager.getInstance()
                .loadAndPlay(channel, song, event);
        return;
    }
}