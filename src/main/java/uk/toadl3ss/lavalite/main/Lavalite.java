package uk.toadl3ss.lavalite.main;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.commandmeta.CommandRegistry;
import uk.toadl3ss.lavalite.commandmeta.init.CommandInitializer;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.utils.Info;
import uk.toadl3ss.lavalite.utils.SetActivity;
import uk.toadl3ss.lavalite.utils.Vanity;
import uk.toadl3ss.lavalite.utils.Version;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class Lavalite {
    // ################################################################################
    // ##                     Lavalite
    // ################################################################################
    public static DefaultShardManagerBuilder builder;
    public static ShardManager jda;
    public static final Logger logger = LoggerFactory.getLogger(Lavalite.class);
    public static String version;
    public static final long START_TIME = System.currentTimeMillis();
    public static final int UNKNOWN_SHUTDOWN_CODE = -991023;
    public static int shutdownCode = UNKNOWN_SHUTDOWN_CODE;
    public static void main(String[] args) throws LoginException, URISyntaxException, IOException {
        System.out.println(Vanity.getVanity());
        Config.init("application.yml");
        byte[] content = Files.readAllBytes(Version.Version().toPath());
        version = new String(content);
        if (Config.INS.getDevelopment()) {
            version = version + " " + "DEV";
        }
        System.out.println("\n" + Info.getInfo());
        logger.info("Starting lavalite v" + version + ".");
        Constants.Init();
        CommandInitializer.initCommands();
        new LavaliteBot();
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