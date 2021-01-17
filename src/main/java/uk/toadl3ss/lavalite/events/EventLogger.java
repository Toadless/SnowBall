package uk.toadl3ss.lavalite.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogger extends ListenerAdapter {
    public static JDA jda;
    public static final Logger log = LoggerFactory.getLogger(EventLogger.class);

    private void send(String msg) {
        log.info(msg);
    }

    @Override
    public void onReady(ReadyEvent event) {
        jda = event.getJDA();
        send(
                "🚀 Received ready event."
        );
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        send(
                "✅ Joined guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
        );
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        send(
                "❌ Left guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
        );
    }
}