/*
 *  MIT License
 *
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of Lavalite and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of Lavalite, and to permit persons to whom Lavalite is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Lavalite.
 *
 * LAVALITE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package net.toaddev.lavalite.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.lavalite.entities.music.MusicManager;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.modules.MusicModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.util.TimeUtils;

@net.toaddev.lavalite.annotation.Command
public class DurationCommand extends Command
{
    public DurationCommand()
    {
        super("duration", "Displays the current songs duration");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
        addAlias("dur", "position");
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel to display the guilds song position.").queue();
            return;
        }

        final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        if (audioPlayer.getPlayingTrack() == null)
        {
            channel.sendMessage("I have no current song playing. No info to show.").queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```md\n");
        stringBuilder.append("< ");
        stringBuilder.append(ctx.getGuild().getName());
        stringBuilder.append(" Song Duration >\n");
        stringBuilder.append("Current Track Position:\n");
        stringBuilder.append("# ");
        stringBuilder.append(TimeUtils.formatDuration(musicManager.getAudioPlayer().getPlayingTrack().getPosition()));
        stringBuilder.append("\n");
        stringBuilder.append("Duration Left:\n");
        stringBuilder.append("# ");
        stringBuilder.append(TimeUtils.formatDuration(musicManager.getAudioPlayer().getPlayingTrack().getDuration() - musicManager.getAudioPlayer().getPlayingTrack().getPosition()));
        stringBuilder.append("\n");
        stringBuilder.append("```");
        channel.sendMessage(stringBuilder.toString()).queue();
    }
}