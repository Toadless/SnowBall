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

import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.modules.CommandsModule;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.CommandFlag;
import net.toaddev.snowball.objects.command.options.CommandOptionString;
import net.toaddev.snowball.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class RegistryCommand extends Command
{
    public RegistryCommand()
    {
        super("registry", "Allows you to modify the bots registry");
        addFlags(CommandFlag.DEVELOPER_ONLY);

        addOptions(
                new CommandOptionString("action", "What do you want to do with the registry?").required()
        );
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        ctx.getEvent().deferReply().queue();
        if (!Config.INS.getDevelopment())
        {
            ctx.getChannel().sendMessage("This command can only be ran in development.").queue();
            return;
        }

        CommandsModule commandsModule = BotController.getModules().get(CommandsModule.class);
        if (ctx.getOption("action").equals("clear"))
        {
            commandsModule.deleteAllCommands();
            ctx.getChannel().sendMessage("**Completely emptying the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        if (ctx.getOption("action").equals("rebuild"))
        {
            commandsModule.deleteAllCommands();
            commandsModule.scanCommands();
            ctx.getChannel().sendMessage("**Completely rebuilding the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        } else
        {
            ctx.getChannel().sendMessage("Please provide a valid method. `clear` | `rebuild`").queue();
        }
    }
}