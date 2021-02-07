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

package net.toaddev.snowball.command.admin;

import net.toaddev.snowball.entities.command.CommandFlag;
import net.toaddev.snowball.entities.command.Command;
import net.toaddev.snowball.main.Launcher;
import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.entities.command.CommandContext;

@net.toaddev.snowball.annotation.Command
public class ReviveCommand extends Command
{
    public ReviveCommand()
    {
        super("revive", null);
        addFlags(CommandFlag.DEVELOPER_ONLY);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("Please provide a shard id to revive.").queue();
            return;
        }
        int shardId = Integer.parseInt(ctx.getArgs()[1]);
        ctx.getChannel().sendMessage("Reviving shard" + " " + shardId).queue();
        Launcher.getInstance(shardId).revive();
    }
}