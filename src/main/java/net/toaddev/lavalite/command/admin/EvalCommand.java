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

package net.toaddev.lavalite.command.admin;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.toaddev.lavalite.entities.command.CommandFlag;
import net.toaddev.lavalite.entities.command.Command;
import net.toaddev.lavalite.util.DiscordUtil;
import org.jetbrains.annotations.NotNull;
import net.toaddev.lavalite.entities.command.CommandEvent;

public class EvalCommand extends Command
{
    private GroovyShell engine;
    private final String imports;
    public EvalCommand()
    {
        super("eval", null);
        addFlags(CommandFlag.DEVELOPER_ONLY);
        addSelfPermissions(Permission.MESSAGE_EMBED_LINKS);
        addAlias("evaluate");
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.api.*;" +
                "import net.dv8tion.jda.api.entities.Activity;" +
                "import net.dv8tion.jda.core.utils.*\n";
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("You need to provide code to evaluate.").queue();
            return;
        }
        String messageArgs = ctx.getMessage().getContentRaw().replaceFirst("^" + ctx.getPrefix() + "eval" + " ", "");
        try
        {
            Object out;
            String status = "Success";

            engine.setProperty("args", messageArgs);
            engine.setProperty("event", ctx.getEvent());
            engine.setProperty("message", ctx.getMessage());
            engine.setProperty("channel", ctx.getChannel());
            engine.setProperty("jda", ctx.getJDA());
            engine.setProperty("guild", ctx.getGuild());
            engine.setProperty("member", ctx.getMember());

            String script = imports + ctx.getMessage().getContentRaw().split("\\s+", 2)[1];

            long start = System.currentTimeMillis();

            try
            {
                out = engine.evaluate(script);
            }
            catch(Exception exception)
            {
                out = exception.getMessage();
                status = "Failed";
            }

            ctx.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Evaluated Result")
                    .addField("Status:", status, true)
                    .addField("Duration:", (System.currentTimeMillis() - start) + "ms", true)
                    .addField("Code:", "```java\n" + ctx.getMessage().getContentRaw().split("\\s+", 2)[1] + "\n```", false)
                    .addField("Result:", out == null ? "No result." : out.toString(), false)
                    .setColor(DiscordUtil.getEmbedColor())
                    .build())
                    .queue();
        }
        catch (Exception e)
        {
            ctx.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}