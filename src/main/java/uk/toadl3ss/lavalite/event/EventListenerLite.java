package uk.toadl3ss.lavalite.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import uk.toadl3ss.lavalite.commandmeta.CommandManager;
import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.data.GuildCache;
import uk.toadl3ss.lavalite.data.database.GuildRegistry;
import uk.toadl3ss.lavalite.main.Launcher;

public class EventListenerLite extends AbstractEventListener {
    // ################################################################################
    // ##                     Message Received Event
    // ################################################################################
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!GuildCache.cache.containsValue(event.getGuild().getId())) {
            GuildCache.addGuild(event.getGuild());
        }
        if (event.getMessage().isWebhookMessage()) {
            return;
        }
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) {
            return;
        }
        if (event.getMember().getUser().isBot()) {
            return;
        }
        if (Launcher.DATABASE_ENABLED) {
            String[] args = event.getMessage().getContentRaw().split(" ");
            Long guildId = Long.parseLong(event.getGuild().getId());
            String guildPrefix = GuildRegistry.getPrefix(guildId);
            if (event.getMessage().getContentRaw().startsWith(guildPrefix)) {
                CommandManager.executeCommand(args, event, guildPrefix);
                return;
            }
            // If the prefix is @<bot> <command>.
            String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
            if (event.getMessage().getContentRaw().startsWith(mention)) {
                CommandManager.executeCommand(args, event, mention);
                return;
            }
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (event.getMessage().getContentRaw().startsWith(Constants.GUILD_PREFIX)) {
            CommandManager.executeCommand(args, event, Constants.GUILD_PREFIX);
            return;
        }
        // If the prefix is @<bot> <command>.
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
        if (event.getMessage().getContentRaw().startsWith(mention)) {
            CommandManager.executeCommand(args, event, mention);
            return;
        }
        return;
    }
}