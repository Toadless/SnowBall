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

package net.toaddev.lavalite.command.fun;

import net.dv8tion.jda.api.utils.data.DataObject;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.util.WebUtil;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandContext;

public class JokeCommand extends Command
{
    public JokeCommand()
    {
        super("joke", null);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        String joke = WebUtil.getReq("http://api.icndb.com/jokes/random");
        if (joke == null)
        {
            ctx.getChannel().sendMessage("Unable to request a joke.").queue();
            return;
        }
        DataObject object = DataObject.fromJson(joke);
        DataObject value = object.getObject("value");
        Object result = value.get("joke");

        result = result.toString().replaceAll("Chuck Norris", ctx.getMember().getUser().getName());
        result = result.toString().replaceAll("&quot;", "\"");

        ctx.getChannel().sendMessage(result.toString()).queue();
    }
}