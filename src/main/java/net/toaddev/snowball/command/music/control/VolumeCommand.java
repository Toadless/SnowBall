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

package net.toaddev.snowball.command.music.control;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.snowball.modules.MusicModule;
import net.toaddev.snowball.objects.Emoji;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.options.CommandOptionInteger;
import net.toaddev.snowball.objects.exception.CommandErrorException;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.objects.music.MusicManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class VolumeCommand extends Command
{
    public VolumeCommand()
    {
        super("setvolume", "Changes the players volume");

        addOptions(
                new CommandOptionInteger("volume", "The volume that you want to set the player to.").required()
        );

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

        if (!memberVoiceState.inVoiceChannel())
        {
            ctx.getEvent().reply("You need to be in a voice channel for this command to work.").queue();
            return;
        } else if (!selfVoiceState.inVoiceChannel())
        {
            ctx.getEvent().reply("I need to be in a voice channel for this to work.").queue();
            return;
        } else if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            ctx.getEvent().reply("You need to be in the same voice channel as me for this to work!").queue();
            return;
        } else if (Integer.parseInt(ctx.getOption("volume")) < 2)
        {
            ctx.getEvent().reply("You need to provide a volume to set between `0 - 200`.").queue();
            return;
        } else
        {
            try
            {
                int volume = Integer.parseInt(ctx.getOption("volume"));
                if (volume <= -1)
                {
                    ctx.getEvent().reply("Please provide a valid volume to set between `0 - 200`.").queue();
                    return;
                }

                if (volume > 200)
                {
                    ctx.getEvent().reply("Please provide a valid volume to set between `0 - 200`.").queue();
                    return;
                }
                final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
                final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
                audioPlayer.setVolume(volume);

                ctx.getEvent().deferReply().queue();

                if (volume < 100)
                {
                    channel.sendMessageFormat(Emoji.VOLUME_DOWN.get() + " Set the volume to: %s", volume).queue();
                    return;
                }
                channel.sendMessageFormat(Emoji.VOLUME_UP.get() + " Set the volume to: %s", volume).queue();
            } catch (NumberFormatException e)
            {
                ctx.getChannel().sendMessage("Please provide a valid number.").queue();
                throw new CommandErrorException("NumberFormatException has been thrown");
            }
        }
    }
}