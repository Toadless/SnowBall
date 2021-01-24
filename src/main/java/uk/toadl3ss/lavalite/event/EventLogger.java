package uk.toadl3ss.lavalite.event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.data.Constants;

public class EventLogger extends ListenerAdapter
{
    public static JDA jda;
    public static final Logger log = LoggerFactory.getLogger(EventLogger.class);

    private void send(String msg) {
        log.info(msg);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        send
                (
                "✅ Joined guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
                )
        ;
        try
        {
            String defaultMessage = Config.INS.getJoin();
            defaultMessage = defaultMessage.replace("{prefix}", Constants.GUILD_PREFIX);
            defaultMessage = defaultMessage.replace("{guildName}", event.getGuild().getName());
            TextChannel systemChannel = event.getGuild().getDefaultChannel();
            if (systemChannel == null) {
                event.getGuild().getDefaultChannel().sendMessage(defaultMessage).queue();
                return;
            }
            systemChannel.sendMessage(defaultMessage).queue();
        } catch (PermissionException e)
        {
            log.info("Unable to send welcome message. Insufficient permissions.");
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {
        send
                (
                "❌ Left guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
                )
        ;
    }
}