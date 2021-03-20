/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.command.music.info;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.objects.music.MusicManager;
import net.toaddev.snowball.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class DurationCommand extends Command
{
    public DurationCommand()
    {
        super("duration", "Displays the current songs duration");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!selfVoiceState.inVoiceChannel())
        {
            ctx.getEvent().reply("I need to be in a voice channel to display the guilds song position.").queue();
            return;
        }

        final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        if (audioPlayer.getPlayingTrack() == null)
        {
            ctx.getEvent().reply("I have no current song playing. No info to show.").queue();
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
        ctx.getEvent().reply(stringBuilder.toString()).queue();
    }
}