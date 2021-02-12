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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.toaddev.snowball.entities.music.MusicManager;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.entities.music.SearchProvider;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.modules.MusicModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.entities.command.CommandContext;

@net.toaddev.snowball.annotation.Command
public class PlayCommand extends Command
{
    public PlayCommand()
    {
        super("play", "Plays the provided song");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
        addAlias("p");
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        String songName = ctx.getMessage().getContentRaw().replaceFirst("^" + ctx.getPrefix() + "play" + " ", "");
        songName = songName.replaceFirst("^" + ctx.getPrefix() + "p" + " ", "");
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (ctx.getArgs().length < 2)
        {
            final MusicManager musicManager = MusicModule.getInstance().getMusicManager(ctx.getGuild());
            if (musicManager.getAudioPlayer().getPlayingTrack() != null)
            {
                boolean paused = musicManager.getScheduler().player.isPaused();
                musicManager.getScheduler().player.setPaused(!paused);
                String status = paused ? "paused" : "playing";
                String newStatus = !paused ? "paused" : "playing";
                channel.sendMessage("Changed the player from **" + status+ "** to **" + newStatus + "**. \nThis event occured because a song is and no arguments were provided!").queue();
                return;
            }
            channel.sendMessage("Please provide a url or search query.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }

        SearchProvider searchProvider = SearchProvider.URL;

        String song = ctx.getArgs()[1];
        if (!BotController.getMusicModule().isUrl(song))
        {
            searchProvider = SearchProvider.YOUTUBE;
            song = songName;
            channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        }
        MusicModule.getInstance()
                .play(ctx, song, searchProvider, true, false);
    }
}