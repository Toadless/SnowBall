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

import org.jetbrains.annotations.NotNull;
import net.toaddev.snowball.entities.command.CommandContext;
import net.toaddev.snowball.entities.command.CommandFlag;
import net.toaddev.snowball.entities.command.Command;

@net.toaddev.snowball.annotation.Command
public class PingCommand extends Command
{
    public PingCommand()
    {
        super("ping", null);
        addFlags(CommandFlag.AUTO_DELETE_MESSAGE);
    }

    @Override
    public void run(@NotNull CommandContext ctx)
    {
        ctx.getJDA().getRestPing().queue(aLong ->
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```md\n");
            stringBuilder.append("< ");
            stringBuilder.append(ctx.getJDA().getSelfUser().getName());
            stringBuilder.append(" Ping >\n");
            stringBuilder.append("Rest Ping: \n");
            stringBuilder.append("# ");
            stringBuilder.append(aLong);
            stringBuilder.append("ms");
            stringBuilder.append("\n");
            stringBuilder.append("Gateway Ping: \n");
            stringBuilder.append("# ");
            stringBuilder.append(ctx.getJDA().getGatewayPing());
            stringBuilder.append("ms");
            stringBuilder.append("\n");
            stringBuilder.append("```");
            ctx.getChannel().sendMessage(stringBuilder.toString()).queue();
        });
    }
}