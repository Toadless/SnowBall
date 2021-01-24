package uk.toadl3ss.lavalite.main;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.event.EventLogger;
import uk.toadl3ss.lavalite.event.ShardListener;

public class BotController extends Launcher
{
    private static final Logger log = LoggerFactory.getLogger(BotController.class);
    private final int shardId;
    public BotController(int shardId, EventListener listener)
    {
        this.shardId = shardId;
        shardListener = new ShardListener();

        log.info("Building shard " + shardId);
        try {
            boolean success = false;
            while (!success)
            {
                JDABuilder builder = JDABuilder.createDefault(Config.INS.getToken())
                        .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES,GatewayIntent.GUILD_EMOJIS)
                        .disableCache(CacheFlag.MEMBER_OVERRIDES)
                        .enableCache(CacheFlag.VOICE_STATE)
                        .setActivity(Activity.playing("v" + Launcher.version + " " + "Starting"))
                        .setBulkDeleteSplittingEnabled(false)
                        .setCompression(Compression.NONE);
                if(listener != null)
                {
                    builder.addEventListeners(listener);
                    builder.addEventListeners(new EventLogger());
                    builder.addEventListeners(shardListener);
                } else
                    {
                    log.warn("Starting a shard without an event listener!");
                }
                if (Config.INS.getNumShards() > 1)
                {
                    builder.useSharding(shardId, Config.INS.getMaxShards());
                }
                jda = builder.build();
                success = true;
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to start JDA shard " + shardId, e);
        }
    }
    int getShardId()
    {
        return shardId;
    }
}