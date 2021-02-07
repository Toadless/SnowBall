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
import net.toaddev.snowball.entities.music.MusicManager;
import net.toaddev.snowball.entities.Emoji;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.entities.exception.CommandErrorException;
import net.toaddev.snowball.modules.MusicModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.entities.command.CommandContext;

@net.toaddev.snowball.annotation.Command
public class VolumeCommand extends Command
{
    public VolumeCommand()
    {
        super("setvolume", "Changes the players volume");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
        addAlias("vol");
    }

    @Override
    public void run(@NotNull CommandContext ctx)
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
        else if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel for this to work.").queue();
            return;
        }
        else if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }
        else if (ctx.getArgs().length < 2)
        {
            channel.sendMessage("You need to provide a volume to set between `0 - 200`.").queue();
            return;
        }
        else
        {
            try
            {
                int volume = Integer.parseInt(ctx.getArgs()[1]);
                if (volume <= -1)
                {
                    channel.sendMessage("Please provide a valid volume to set between `0 - 200`.").queue();
                    return;
                }

                if (volume > 200)
                {
                    channel.sendMessage("Please provide a valid volume to set between `0 - 200`.").queue();
                    return;
                }
                final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
                final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
                audioPlayer.setVolume(volume);

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