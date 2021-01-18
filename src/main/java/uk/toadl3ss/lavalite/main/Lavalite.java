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
import uk.toadl3ss.lavalite.events.EventLogger;

public class Lavalite extends Launcher {
    private static final Logger log = LoggerFactory.getLogger(Lavalite.class);
    private final int shardId;
    public Lavalite(int shardId, EventListener listener) {
        this.shardId = shardId;
        log.info("Building shard " + shardId);
        try {
            boolean success = false;
            while (!success) {
                Launcher.builder = JDABuilder.createDefault(
                        Config.INS.getToken(),
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_EMOJIS
                );
                Launcher.builder.disableCache(
                        CacheFlag.MEMBER_OVERRIDES
                );
                Launcher.builder.enableCache(
                        CacheFlag.VOICE_STATE
                );
                Launcher.builder.setActivity(Activity.playing("v" + Launcher.version + " " + "Starting"));
                Launcher.builder.setBulkDeleteSplittingEnabled(false);
                Launcher.builder.setCompression(Compression.NONE);
                Launcher.builder.addEventListeners(new EventLogger());
                if(listener != null) {
                    builder.addEventListeners(listener);
                } else {
                    log.warn("Starting a shard without an event listener!");
                }
                if (Config.INS.getNumShards() > 1) {
                    builder.useSharding(shardId, Config.INS.getNumShards());
                }
                jda = builder.build();
                success = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to start JDA shard " + shardId, e);
        }
    }
    int getShardId() {
        return shardId;
    }
}