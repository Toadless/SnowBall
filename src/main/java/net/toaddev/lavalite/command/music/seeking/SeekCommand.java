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

package net.toaddev.lavalite.command.music.seeking;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.toaddev.lavalite.entities.music.MusicManager;
import net.toaddev.lavalite.modules.MusicModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.entities.exception.CommandErrorException;
import net.toaddev.lavalite.util.TimeUtils;

@net.toaddev.lavalite.annotation.Command
public class SeekCommand extends Command
{
    public SeekCommand()
    {
        super("seek", "Seeks to a provided position in the track");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
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

        final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        if (audioPlayer.getPlayingTrack() == null)
        {
            channel.sendMessage("No current playing song.").queue();
            return;
        }
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("Please provide a position to seek to.").queue();
            return;
        }
        try
        {
            int amount = Integer.parseInt(ctx.getArgs()[1]);
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