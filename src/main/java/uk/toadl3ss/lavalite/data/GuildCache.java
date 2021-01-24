package uk.toadl3ss.lavalite.data;

import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class GuildCache {
    // Guild caching is only used for cleaning out voice channels
    // so if a guilds fails to get cached it isn't a major issue
    // it just uses up more bandwidth.
    public static HashMap<Long, Guild> cache = new HashMap<>();
    public static void addGuild(Guild guild) {
        cache.put(guild.getIdLong(), guild);
    }
}