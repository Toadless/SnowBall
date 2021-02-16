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

import net.toaddev.snowball.objects.command.CommandFlag;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.modules.DatabaseModule;
import net.toaddev.snowball.modules.SettingsModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.objects.command.CommandContext;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class PrefixCommand extends Command
{
    public PrefixCommand()
    {
        super("prefix", null);
        addFlags(CommandFlag.SERVER_ADMIN_ONLY);
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        long guildId = Long.parseLong(ctx.getGuild().getId());
        if (!BotController.DATABASE_ENABLED)
        {
            return;
        }
        else if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessageFormat("Your prefix is: `%s`", BotController.getModules().get(DatabaseModule.class).getPrefix(guildId)).queue();
            return;
        }
        else if (ctx.getArgs()[1].length() > 5)
        {
            ctx.getChannel().sendMessage("The max amount of characters you can have in a prefix is `5`.").queue();
            return;
        }
        else
        {
            BotController.getModules().get(SettingsModule.class).setGuildPrefix(guildId, ctx.getArgs()[1]);
            ctx.getChannel().sendMessageFormat("Set the guilds prefix to: `%s`", ctx.getArgs()[1]).queue();
        }
    }
}