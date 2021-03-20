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

package net.toaddev.snowball.command.fun;

import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.options.CommandOptionString;
import net.toaddev.snowball.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class EmoteCommand extends Command
{
    public EmoteCommand()
    {
        super("emote", "Gives you an emotes unicode.");

        addOptions(
                new CommandOptionString("emote", "The emote that you want infomation about").required()
        );
    }

    @Override
    public void run(@Nonnull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        try
        {
            final String in = ctx.getOption("emote");
            final StringBuilder out = new StringBuilder();
            for (int i = 0; i < in.length(); i++)
            {
                final char ch = in.charAt(i);
                if (ch <= 127) out.append(ch);
                else out.append("\\u").append(String.format("%04x", (int) ch));
            }

            in.codePoints().forEachOrdered(code ->
            {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("**Emote Info** \n");
                stringBuilder.append("[`");
                stringBuilder.append(out.toString());
                stringBuilder.append("`]  ");
                stringBuilder.append(in);
                stringBuilder.append("     -     *");
                stringBuilder.append(Character.getName(code));
                stringBuilder.append("*");
                ctx.getChannel().sendMessage(stringBuilder.toString()).queue();
            });
        } catch (Exception e)
        {
            ctx.getChannel().sendMessage("Unable to convert emote to unicode!").queue();
        }
    }
}