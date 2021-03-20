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

package net.toaddev.snowball.command.music.seeking;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.options.CommandOptionInteger;
import net.toaddev.snowball.objects.exception.CommandErrorException;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.objects.music.MusicManager;
import net.toaddev.snowball.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class SeekCommand extends Command
{
    public SeekCommand()
    {
        super("seek", "Seeks to a provided position in the track");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);

        addOptions(
                new CommandOptionInteger("position", "The position that you want to seek to.").required()
        );
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel())
        {
            ctx.getEvent().reply("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            ctx.getEvent().reply("I need to be in a voice channel for this to work.").queue();
            return;
        }
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            ctx.getEvent().reply("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }

        final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        if (audioPlayer.getPlayingTrack() == null)
        {
            ctx.getEvent().reply("No current playing song.").queue();
            return;
        }
        try
        {
            ctx.getEvent().acknowledge().queue();
            int amount = Integer.parseInt(ctx.getOption("position"));
            amount = (amount * 1000);
            if (musicManager.getScheduler().player.getPlayingTrack().getDuration() < Long.parseLong(String.valueOf(amount)))
            {
                ctx.getChannel().sendMessage("The tracks duration is less than: `" + TimeUtils.formatDuration(Long.parseLong(String.valueOf(amount))) + "`.").queue();
                return;
            }
            musicManager.getScheduler().player.getPlayingTrack().setPosition(Long.parseLong(String.valueOf(amount)));
            ctx.getChannel().sendMessage("The tracks position has been set to: `" + TimeUtils.formatDuration(Long.parseLong(String.valueOf(amount))) + "`.").queue();
        } catch (NumberFormatException e)
        {
            ctx.getChannel().sendMessage("Please provide a valid number.").queue();
            throw new CommandErrorException("NumberFormatException has been thrown");
        }
    }
}