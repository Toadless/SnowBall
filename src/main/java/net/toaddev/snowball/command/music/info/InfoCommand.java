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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.objects.music.MusicManager;
import net.toaddev.snowball.util.DiscordUtil;
import net.toaddev.snowball.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class InfoCommand extends Command
{
    public InfoCommand()
    {
        super("info", "Displays info about the guilds player");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!selfVoiceState.inVoiceChannel())
        {
            ctx.getEvent().reply("I need to be in a voice channel to display the guilds play information.").queue();
            return;
        }

        final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        if (audioPlayer.getPlayingTrack() == null)
        {
            ctx.getEvent().reply("I have no current song playing. No info to show.").queue();
            return;
        }

        int queueDur;
        queueDur = 0;

        queueDur += musicManager.getScheduler().getQueue().stream().mapToInt(track -> (int) track.getDuration()).sum();

        ctx.getChannel().sendMessage(new EmbedBuilder()
                .setTitle(ctx.getGuild().getName() + " Music Info")
                .addField("Current Song", musicManager.getAudioPlayer().getPlayingTrack().getInfo().title, true)
                .addField("Current Song Pos", TimeUtils.formatDuration(musicManager.getAudioPlayer().getPlayingTrack().getPosition()), true)
                .addBlankField(true)
                .addField("Queue Size", String.valueOf(musicManager.getScheduler().getQueue().size()), true)
                .addField("Queue Duration", TimeUtils.formatDuration(queueDur), true)
                .addBlankField(true)
                .addField("Looping", String.valueOf(musicManager.getScheduler().repeating).toUpperCase(), true)
                .addField("Paused", String.valueOf(musicManager.getScheduler().player.isPaused()).toUpperCase(), true)
                .addBlankField(true)
                .setColor(DiscordUtil.getEmbedColor())
                .build()).queue();
    }
}