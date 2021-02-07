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

package net.toaddev.snowball.command.maintenance;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.main.Launcher;
import net.toaddev.snowball.util.DiscordUtil;
import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.entities.command.CommandContext;

@net.toaddev.snowball.annotation.Command
public class StatsCommand extends Command
{
    public StatsCommand()
    {
        super("stats", null);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        ctx.getChannel().sendMessage(new EmbedBuilder()
                .setTitle(ctx.getJDA().getSelfUser().getAsTag() + " information")
                .addField("JVM Version",System.getProperty("java.version"), true)
                .addField("JDA Version", JDAInfo.VERSION, true)
                .addBlankField(true)
                .addField("Thread Count", String.valueOf(java.lang.Thread.activeCount()), true)
                .addField("Memory Usage", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000 + "MB", true)
                .addBlankField(true)
                .addField("Shard info", ctx.getJDA().getShardInfo().getShardString(), true)
                .addField("Server count", String.valueOf(Launcher.getAllGuilds().size()), true)
                .addBlankField(true)
                .setColor(DiscordUtil.getEmbedColor())
                .build()).queue();
    }
}