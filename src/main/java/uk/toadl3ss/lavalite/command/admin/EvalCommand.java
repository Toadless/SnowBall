package uk.toadl3ss.lavalite.command.admin;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandFlags;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
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
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        if (args.length < 2)
        {
            event.getChannel().sendMessage("You need to provide code to evaluate.").queue();
            return;
        }
        String messageArgs = event.getMessage().getContentRaw().replaceFirst("^" + prefix + "eval" + " ", "");
        try
        {
            engine.setProperty("args", messageArgs);
            engine.setProperty("event", event);
            engine.setProperty("message", event.getMessage());
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("jda", event.getJDA());
            engine.setProperty("guild", event.getGuild());
            engine.setProperty("member", event.getMember());

            String script = imports + event.getMessage().getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            event.getChannel().sendTyping().queue();

            String output = out == null ? "Executed without error" : out.toString();

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(DiscordUtil.getEmbedColor())
                    .setTitle("Eval")
                    .addField("**Input**", "```java\n" + messageArgs + "\n```", false)
                    .addField("**Output**", "```java\n" + output + "```", false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        catch (Exception e)
        {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}