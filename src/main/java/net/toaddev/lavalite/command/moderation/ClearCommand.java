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

package net.toaddev.lavalite.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.entities.command.CommandEvent;
import net.toaddev.lavalite.entities.command.CommandFlag;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class ClearCommand extends Command
{
    public ClearCommand()
    {
        super("clear", null);
        addFlags(CommandFlag.SERVER_ADMIN_ONLY);
        addMemberPermissions(Permission.MESSAGE_MANAGE);
        addSelfPermissions(Permission.MESSAGE_MANAGE);
        addAlias("cleanup");
    }

    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("Please provide an amount of messages to delete.").queue();
            return;
        }

        if (Integer.parseInt(ctx.getArgs()[1]) > 100) {
            ctx.getChannel().sendMessage("Make sure that your range is `1-100`.").queue();
            return;
        }

        try {
            final MessageChannel channel = ctx.getChannel();
            MessageHistory history = new MessageHistory(channel);
            history.retrievePast(Integer.parseInt((ctx.getArgs()[1]+1))).queue(channel::purgeMessages);
            channel.sendMessage("Successfully deleted" + " " + ctx.getArgs()[1] + " " + "messages.")
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
        } catch (IllegalArgumentException e) {
            ctx.getChannel().sendMessage("I cant delete messages older than 2 weeks!").queue();
        }
    }
}