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

package net.toaddev.lavalite.command.util;

import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.modules.CommandsModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.entities.command.Command;

public class HelpCommand extends Command
{
    public HelpCommand()
    {
        super("help", null);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        if (ctx.getArgs().length < 2)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ctx.getMember().getUser().getName());
            stringBuilder.append(": Say `");
            stringBuilder.append(ctx.getPrefix());
            stringBuilder.append("commands` to learn what this bot can do! \n");
            stringBuilder.append("The prefix for this guild is `");
            stringBuilder.append(ctx.getPrefix());
            stringBuilder.append("`");
            ctx.getChannel().sendMessage(stringBuilder.toString()).queue();
            return;
        }
        Command command = Launcher.getModules().get(CommandsModule.class).getCommand(ctx.getArgs()[1]);
        if (command == null)
        {
            ctx.getChannel().sendMessage("Unknown command: `" + ctx.getArgs()[1] + "`.");
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