package uk.toadl3ss.lavalite.command.admin;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;
import uk.toadl3ss.lavalite.util.DiscordUtil;

public class EvalCommand extends Command
{
    private GroovyShell engine;
    private final String imports;
    public EvalCommand()
    {
        super("eval", null);
        addFlag(CommandFlags.DEVELOPER_ONLY);
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
            engine.setProperty("args", messageArgs);
            engine.setProperty("event", ctx.getEvent());
            engine.setProperty("message", ctx.getMessage());
            engine.setProperty("channel", ctx.getChannel());
            engine.setProperty("jda", ctx.getJDA());
            engine.setProperty("guild", ctx.getGuild());
            engine.setProperty("member", ctx.getMember());

            String script = imports + ctx.getMessage().getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            ctx.getChannel().sendTyping().queue();

            String output = out == null ? "Executed without error" : out.toString();

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(DiscordUtil.getEmbedColor())
                    .setTitle("Eval")
                    .addField("**Input**", "```java\n" + messageArgs + "\n```", false)
                    .addField("**Output**", "```java\n" + output + "```", false);
            ctx.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        catch (Exception e)
        {
            ctx.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}