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

package net.toaddev.snowball.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.CommandFlag;
import net.toaddev.snowball.objects.command.options.CommandOptionInteger;
import net.toaddev.snowball.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class ClearCommand extends Command
{
    public ClearCommand()
    {
        super("clear", "Clears the specified messages.");
        addFlags(CommandFlag.SERVER_ADMIN_ONLY);
        addMemberPermissions(Permission.MESSAGE_MANAGE);
        addSelfPermissions(Permission.MESSAGE_MANAGE);

        addOptions(
                new CommandOptionInteger("amount", "The amount of messages").required()
        );
    }

    @Override
    public void run(@Nonnull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        ctx.getEvent().deferReply().queue();

        if (Integer.parseInt(ctx.getOption("amount")) > 100)
        {
            ctx.getChannel().sendMessage("Make sure that your range is `1-100`.").queue();
            return;
        }

        try
        {
            final MessageChannel channel = ctx.getChannel();
            MessageHistory history = new MessageHistory(channel);
            history.retrievePast(Integer.parseInt((ctx.getOption("amount") + 1))).queue(channel::purgeMessages);
            channel.sendMessage("Successfully deleted" + " " + ctx.getOption("amount") + " " + "messages.")
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
        } catch (IllegalArgumentException e)
        {
            ctx.getChannel().sendMessage("I cant bulk delete messages older than 2 weeks!").queue();
        }
    }
}