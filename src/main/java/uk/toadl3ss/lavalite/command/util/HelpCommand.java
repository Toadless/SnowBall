package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.RestAction;
import uk.toadl3ss.lavalite.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandUtil;

import java.util.ArrayList;
import java.util.List;


public class HelpCommand extends Command implements ICommandUtil {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        StringBuilder helpString = new StringBuilder();
        helpString.append("```md\n");
        String title = "< {name} Music Commands >\n";
        title = title.replace("{name}", event.getJDA().getSelfUser().getName());
        helpString.append(title);

        List<String> helpList = new ArrayList<>();
        CommandRegistry.registry.forEach(((s, command) -> {
            String help = command.getHelp();
            if (helpList.contains(help)) {
                return;
            }
            if (help == null) {
                return;
            }
            helpString.append(prefix + s + "\n");
            helpString.append("#" + help + "\n");
            helpList.add(help);
        }));

        helpString.append("\n```");

        RestAction<PrivateChannel> privateChannel = event.getMember().getUser().openPrivateChannel();
        privateChannel
                .flatMap(channel -> channel.sendMessage(helpString))
                .flatMap(channel -> event.getChannel().sendMessage(event.getMember().getUser().getName() + ": Documentation has been sent to your DMs!"))
                .queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE).handle(ErrorResponse.CANNOT_SEND_TO_USER, (e) -> {
                    event.getChannel().sendMessage(helpString.toString()).queue();
                }));
    }
    @Override
    public String getHelp() {
        return null;
    }
}