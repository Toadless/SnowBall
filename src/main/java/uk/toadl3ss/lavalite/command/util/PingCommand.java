package uk.toadl3ss.lavalite.command.util;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandMusic;

public class PingCommand extends Command implements ICommandMusic {
    @Override
    public void onInvoke(String[] args, GuildMessageReceivedEvent event, String prefix) {
        event.getJDA().getRestPing().queue(aLong -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```md\n");
            stringBuilder.append("< ");
            stringBuilder.append(event.getJDA().getSelfUser().getName());
            stringBuilder.append(" Ping >\n");
            stringBuilder.append("Rest Ping: \n");
            stringBuilder.append("# ");
            stringBuilder.append(aLong);
            stringBuilder.append("ms");
            stringBuilder.append("\n");
            stringBuilder.append("Gateway Ping: \n");
            stringBuilder.append("# ");
            stringBuilder.append(event.getJDA().getGatewayPing());
            stringBuilder.append("ms");
            stringBuilder.append("\n");
            stringBuilder.append("```");
            event.getChannel().sendMessage(stringBuilder.toString()).queue();
        });
    }

    @Override
    public String getHelp() {
        return null;
    }
}