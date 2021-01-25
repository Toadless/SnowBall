package uk.toadl3ss.lavalite.command.util;

import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandRegistry;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

public class HelpCommand extends Command
{
    public HelpCommand()
    {
        super("help", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        if (ctx.getArgs().length < 2)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ctx.getMember().getUser().getName());
            stringBuilder.append(": Say `");
            stringBuilder.append(ctx.getPrefix());
            stringBuilder.append("commands` to learn what this bot can do! \n");
            stringBuilder.append("The prefix for this guild is `");
            stringBuilder.append(ctx.getPrefix());
            stringBuilder.append("`");
            ctx.getChannel().sendMessage(stringBuilder.toString()).queue();
            return;
        }
        Command command = CommandRegistry.registry.get(ctx.getArgs()[1]);
        if (command == null)
        {
            ctx.getChannel().sendMessage("Unknown command: `" + ctx.getArgs()[1] + "`.");
            return;
        }
        if (command.getHelp() == null)
        {
            ctx.getChannel().sendMessage("Nothing to display.").queue();
            return;
        }
        StringBuilder commandHelp = new StringBuilder();
        commandHelp.append("```md\n");
        commandHelp.append("< Command Help >");
        commandHelp.append("\n");
        commandHelp.append("# ");
        commandHelp.append(command.getHelp());
        commandHelp.append("```");
        ctx.getChannel().sendMessage(commandHelp.toString()).queue();
    }
}