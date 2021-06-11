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

import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.modules.CommandsModule;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.options.CommandOptionString;
import net.toaddev.snowball.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class HelpCommand extends Command
{
    public HelpCommand()
    {
        super("help", "Gives you help for each of the commands.");

        addOptions(
                new CommandOptionString("command", "The command that you want help about.")
        );
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        ctx.getEvent().deferReply().queue();
        if (ctx.getOption("command") == null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ctx.getMember().getUser().getName());
            stringBuilder.append(": Say `/");
            stringBuilder.append("commands` to learn what this bot can do! \n");
            stringBuilder.append("`");
            ctx.getChannel().sendMessage(stringBuilder.toString()).queue();
            return;
        }
        Command command = BotController.getModules().get(CommandsModule.class).getCommand(ctx.getOption("command"));
        if (command == null)
        {
            ctx.getChannel().sendMessage("Unknown command: `" + ctx.getOption("command") + "`.");
            return;
        }
        if (command.getDescription() == null)
        {
            ctx.getChannel().sendMessage("Nothing to display.").queue();
            return;
        }
        StringBuilder commandHelp = new StringBuilder();
        commandHelp.append("```md\n");
        commandHelp.append("< Command Help >");
        commandHelp.append("\n");
        commandHelp.append("# ");
        commandHelp.append(command.getDescription());
        commandHelp.append("```");
        ctx.getChannel().sendMessage(commandHelp.toString()).queue();
    }
}