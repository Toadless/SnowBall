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

import net.toaddev.lavalite.entities.command.Command;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;
import net.toaddev.lavalite.data.Constants;

public class InviteCommand extends Command
{

    public InviteCommand()
    {
        super("invite", null);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        if (!Constants.invite)
        {
            return;
        }
        String str = "https://discord.com/api/oauth2/authorize?client_id=" + ctx.getJDA().getSelfUser().getId() + "&permissions=267911025&scope=bot";
        ctx.getChannel().sendMessageFormat("My invite: %s", str).queue();
    }
}