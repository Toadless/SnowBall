package net.toaddev.lavalite.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.toaddev.lavalite.entities.command.CommandManager;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.database.GuildRegistry;

public class EventListenerLite extends AbstractEventListener
{
    // ################################################################################
    // ##                     Message Received Event
    // ################################################################################
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (event.getMessage().isWebhookMessage())
        {
            return;
        }
        if (event.getAuthor().equals(event.getJDA().getSelfUser()))
        {
            return;
        }
        if (event.getMember().getUser().isBot())
        {
            return;
        }
        if (Launcher.DATABASE_ENABLED)
        {
            String[] args = event.getMessage().getContentRaw().split(" ");
            Long guildId = Long.parseLong(event.getGuild().getId());
            String guildPrefix = GuildRegistry.getPrefix(guildId);
            if (event.getMessage().getContentRaw().startsWith(guildPrefix))
            {
                CommandManager.executeCommand(args, event, guildPrefix);
                return;
            }
            // If the prefix is @<bot> <command>.
            String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
            if (event.getMessage().getContentRaw().startsWith(mention))
            {
                CommandManager.executeCommand(args, event, mention);
                return;
            }
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (event.getMessage().getContentRaw().startsWith(Constants.GUILD_PREFIX))
        {
            CommandManager.executeCommand(args, event, Constants.GUILD_PREFIX);
            return;
        }
        // If the prefix is @<bot> <command>.
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
        if (event.getMessage().getContentRaw().startsWith(mention))
        {
            CommandManager.executeCommand(args, event, mention);
            return;
        }
        return;
    }
}