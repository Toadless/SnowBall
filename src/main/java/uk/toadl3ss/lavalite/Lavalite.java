package uk.toadl3ss.lavalite;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.cache.ShardCacheView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.commandmeta.init.CommandInitializer;
import uk.toadl3ss.lavalite.events.EventListenerLite;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.events.EventLogger;
import uk.toadl3ss.lavalite.utils.Info;
import uk.toadl3ss.lavalite.utils.SetActivity;
import uk.toadl3ss.lavalite.utils.Vanity;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lavalite {
    // ################################################################################
    // ##                     Lavalite
    // ################################################################################
    public static DefaultShardManagerBuilder builder;
    public static ShardManager jda;
    public static final Logger logger = LoggerFactory.getLogger(Lavalite.class);
    public static String version = "1.0.0";
    public static final long START_TIME = System.currentTimeMillis();
    public static final int UNKNOWN_SHUTDOWN_CODE = -991023;
    public static int shutdownCode = UNKNOWN_SHUTDOWN_CODE;
    public static void main(String[] args) throws LoginException {
        System.out.println(Vanity.getVanity());
        Config.init("application.yml");
        if (Config.INS.getDevelopment()) {
            version = version + " " + "DEV";
        }
        System.out.println("\n" + Info.getInfo());
        Constants.Init();
        builder = DefaultShardManagerBuilder.createDefault(
                Config.INS.getToken(),
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        );
        builder.disableCache(
                CacheFlag.MEMBER_OVERRIDES
        );
        builder.enableCache(
                CacheFlag.VOICE_STATE
        );
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.setShardsTotal(-1);
        builder.addEventListeners(new EventListenerLite());
        builder.addEventListeners(new EventLogger());
        jda = builder.build();
        CommandInitializer.initCommands();
        SetActivity.SetActivity(jda);
    }

    public static void shutdown(int code) {
        uk.toadl3ss.lavalite.utils.Logger.info("Shutting down with exit code " + code);
        CommandRegistry.registry.clear();
        CommandRegistry.logger.info("Clearing all command registry.");
        shutdownCode = code;
        getJda().shutdown();
        System.exit(code);
    }

    public static ShardManager getJda() {
        return jda;
    }

    public static void reviveShard(int sId) {
        getJda().restart(sId);
        uk.toadl3ss.lavalite.utils.Logger.warn("Reviving shard" + " " + sId + ".");
    }

    public static int getAllGuilds() {
        int shardCache = getJda().getShardsTotal();
        return shardCache;
    }

    public static int getAllUsersAsMap() {
        int size = getJda().getUsers().size();
        return size;
    }

    public static int getAllShards() {
        int size = getJda().getShardsTotal();
        return size;
    }
}