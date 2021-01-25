package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.lavalite.entities.command.CommandEvent;
import uk.toadl3ss.lavalite.entities.command.CommandRegistry;
import uk.toadl3ss.lavalite.entities.command.CommandFlags;
import uk.toadl3ss.lavalite.entities.command.abs.Command;

import java.util.ArrayList;
import java.util.List;


public class CommandsCommand extends Command
{
    public CommandsCommand()
    {
        super("commands", null);
        addFlag(CommandFlags.DEFAULT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        StringBuilder helpString = new StringBuilder();
        helpString.append("```md\n");
        String title = "< {name} Music Commands >\n";
        title = title.replace("{name}", ctx.getJDA().getSelfUser().getName());
        helpString.append(title);

        List<String> helpList = new ArrayList<>();
        CommandRegistry.registry.forEach(((s, command) ->
        {
            String help = command.getHelp();
            if (helpList.contains(help)) {
                return;
            }
            if (help == null) {
                return;
            }
            helpString.append(ctx.getPrefix() + s + "\n");
            helpString.append("#" + help + "\n");
            helpList.add(help);
        }));

        helpString.append("\n```");

        RestAction<PrivateChannel> privateChannel = ctx.getMember().getUser().openPrivateChannel();
        privateChannel
                .flatMap(channel -> channel.sendMessage(helpString))
                .flatMap(channel -> ctx.getChannel().sendMessage(ctx.getMember().getUser().getName() + ": Documentation has been sent to your DMs!"))
                .queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE).handle(ErrorResponse.CANNOT_SEND_TO_USER, (e) ->
                {
                    ctx.getChannel().sendMessage(helpString.toString()).queue();
                }));
    }
}