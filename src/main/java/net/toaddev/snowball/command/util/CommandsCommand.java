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

package net.toaddev.snowball.command.util;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.modules.CommandsModule;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.util.MessageUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class CommandsCommand extends Command
{
    public CommandsCommand()
    {
        super("commands", "Displays all of the bots commands,");
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        ctx.getEvent().deferReply().queue();

        Map<String, Command> commandMap = BotController.getModules().get(CommandsModule.class).getCommands();
        Collection<Command> cmds = new LinkedList<>();

        commandMap.forEach((s, command) ->
        {
            if (command.getDescription() == null)
            {
                return;
            }

            cmds.add(command);
        });

        processMessage(cmds, ctx.getChannel(), ctx);
    }

    public void processMessage(Collection<Command> cmds, MessageChannel channel, CommandContext ctx)
    {
        MessageUtils.sendCommands(cmds, ctx.getModules(), channel, ctx.getMember().getUser().getIdLong(), "Commands: ");
    }
}