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
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";

        String[] args = event.getMessage().getContentRaw().split(" ");

        long guildId = Long.parseLong(event.getGuild().getId());

        if (event.getMessage().isWebhookMessage())
        {
            return;
        }
        else if (event.getAuthor().equals(event.getJDA().getSelfUser()))
        {
            return;
        }
        else if (event.getMember().getUser().isBot())
        {
            return;
        }
        else if (Launcher.DATABASE_ENABLED)
        {
            String guildPrefix = GuildRegistry.getPrefix(guildId);
            if (event.getMessage().getContentRaw().startsWith(guildPrefix))
            {
                this.executeCommand(args, event, guildPrefix);
                return;
            }
            else if (event.getMessage().getContentRaw().startsWith(mention))
            {
                this.executeCommand(args, event, guildPrefix);
                return;
            }
            return;
        }
        else if (event.getMessage().getContentRaw().startsWith(Constants.GUILD_PREFIX))
        {
            this.executeCommand(args, event, Constants.GUILD_PREFIX);
            return;
        }
        else if (event.getMessage().getContentRaw().startsWith(mention))
        {
            this.executeCommand(args, event, Constants.GUILD_PREFIX);
            return;
        }
        return;
    }

    public void executeCommand(String[] args, GuildMessageReceivedEvent event, String prefix)
    {
        String[] a = args;
        GuildMessageReceivedEvent e = event;
        String p = prefix;
        CommandManager manager = Launcher.getCommandManager();
        manager.handleCommand(a, e, p);
    }
}