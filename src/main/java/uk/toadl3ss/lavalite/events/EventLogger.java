package uk.toadl3ss.lavalite.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.main.Lavalite;

public class EventLogger extends ListenerAdapter {
    private int shards = 0;
    public static JDA jda;
    public static final Logger log = LoggerFactory.getLogger(EventLogger.class);

    private void send(String msg) {
        log.info(msg);
    }

    @Override
    public void onReady(ReadyEvent event) {
        jda = event.getJDA();
        ShardManager shardManager = jda.getShardManager();
        shards++;
        send("Shard" + " " + jda.getShardInfo().getShardId() + " (" + jda.getGuildCache().size() + " guilds) launched.");
        if(shards == jda.getShardInfo().getShardTotal()){
            long shardAmount = shardManager.getShardCache().size();
            long totalGuilds = shardManager.getGuildCache().size();
            send("Loaded Lavalite v" + Lavalite.version + " " + "loaded with" + " " + shardAmount + " shard(s) and" + " " + totalGuilds + " guild(s).");
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        send(
                "✅ Joined guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
        );
        try {
            String defaultMessage = Config.INS.getJoin();
            defaultMessage = defaultMessage.replace("{prefix}", Constants.GUILD_PREFIX);
            defaultMessage = defaultMessage.replace("{guildName}", event.getGuild().getName());
            TextChannel systemChannel = event.getGuild().getDefaultChannel();
            if (systemChannel == null) {
                event.getGuild().getDefaultChannel().sendMessage(defaultMessage).queue();
                return;
            }
            systemChannel.sendMessage(defaultMessage).queue();
        } catch (PermissionException e) {
            log.info("Unable to send welcome message. Insufficient permissions.");
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        send(
                "❌ Left guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
        );
    }
}