package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandUtil;

public class HelpCommand extends Command implements ICommandUtil {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.getMember().getUser().getName());
        stringBuilder.append(": Say `");
        stringBuilder.append(prefix);
        stringBuilder.append("commands` to learn what this bot can do! \n");
        stringBuilder.append("The prefix for this guild is `");
        stringBuilder.append(prefix);
        stringBuilder.append("`");
        event.getChannel().sendMessage(stringBuilder.toString()).queue();
    }

    @Override
    public String getHelp() {
        return null;
    }
}