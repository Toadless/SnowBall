package uk.toadl3ss.lavalite.main;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.events.EventListenerLite;
import uk.toadl3ss.lavalite.events.EventLogger;

import javax.security.auth.login.LoginException;

public class LavaliteBot {
    public LavaliteBot() throws LoginException {
        Lavalite.builder = DefaultShardManagerBuilder.createDefault(
                Config.INS.getToken(),
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        );
        Lavalite.builder.disableCache(
                CacheFlag.MEMBER_OVERRIDES
        );
        Lavalite.builder.enableCache(
                CacheFlag.VOICE_STATE
        );
        Lavalite.builder.setBulkDeleteSplittingEnabled(false);
        Lavalite.builder.setCompression(Compression.NONE);
        Lavalite.builder.setShardsTotal(-1);
        Lavalite.builder.addEventListeners(new EventListenerLite());
        Lavalite.builder.addEventListeners(new EventLogger());
        Lavalite.jda = Lavalite.builder.build();
    }
}