package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.entities.commandmeta.CommandFlags;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.ICommandUtil;

public class HelpCommand extends Command implements ICommandUtil
{
    public HelpCommand()
    {
        super("help", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        if (args.length < 2)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(event.getMember().getUser().getName());
            stringBuilder.append(": Say `");
            stringBuilder.append(prefix);
            stringBuilder.append("commands` to learn what this bot can do! \n");
            stringBuilder.append("The prefix for this guild is `");
            stringBuilder.append(prefix);
            stringBuilder.append("`");
            event.getChannel().sendMessage(stringBuilder.toString()).queue();
            return;
        }
        Command command = CommandRegistry.registry.get(args[1]);
        if (command == null)
        {
            event.getChannel().sendMessage("Unknown command: `" + args[1] + "`.");
            return;
        }
        if (command.getHelp() == null)
        {
            event.getChannel().sendMessage("Nothing to display.").queue();
            return;
        }
        StringBuilder commandHelp = new StringBuilder();
        commandHelp.append("```md\n");
        commandHelp.append("< Command Help >");
        commandHelp.append("\n");
        commandHelp.append("# ");
        commandHelp.append(command.getHelp());
        commandHelp.append("```");
        event.getChannel().sendMessage(commandHelp.toString()).queue();
    }
}