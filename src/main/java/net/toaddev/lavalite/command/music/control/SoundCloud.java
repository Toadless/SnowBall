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

package net.toaddev.lavalite.command.music.control;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.entities.music.SearchProvider;
import net.toaddev.lavalite.modules.MusicModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;

@net.toaddev.lavalite.annotation.Command
public class SoundCloud extends Command
{
    public SoundCloud() {
        super("soundcloud", "Plays music from soundcloud");
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
        addAlias("sc");
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        String songName = ctx.getMessage().getContentRaw().replaceFirst("^" + ctx.getPrefix() + "soundcloud" + " ", "");
        songName = songName.replaceFirst("^" + ctx.getPrefix() + "sc" + " ", "");
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
            channel.sendMessage("Please provide a search query.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        MusicModule.getInstance()
                .play(ctx, songName, SearchProvider.SOUNDCLOUD, true, false);
        return;
    }
}