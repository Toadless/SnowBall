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

import net.toaddev.lavalite.entities.command.CommandFlag;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.modules.DatabaseModule;
import net.toaddev.lavalite.modules.SettingsModule;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;

public class PrefixCommand extends Command
{
    public PrefixCommand()
    {
        super("prefix", null);
        addFlags(CommandFlag.SERVER_ADMIN_ONLY);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        long guildId = Long.parseLong(ctx.getGuild().getId());
        if (!Launcher.DATABASE_ENABLED)
        {
            return;
        }
        else if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessageFormat("Your prefix is: `%s`", Launcher.getModules().get(DatabaseModule.class).getPrefix(guildId)).queue();
            return;
        }
        else if (ctx.getArgs()[1].length() > 5)
        {
            ctx.getChannel().sendMessage("The max amount of characters you can have in a prefix is `5`.").queue();
            return;
        }
        else
        {
            Launcher.getModules().get(SettingsModule.class).setGuildPrefix(guildId, ctx.getArgs()[1]);
            ctx.getChannel().sendMessageFormat("Set the guilds prefix to: `%s`", ctx.getArgs()[1]).queue();
        }
    }
}