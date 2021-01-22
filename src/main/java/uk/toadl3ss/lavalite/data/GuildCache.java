package uk.toadl3ss.lavalite.data;

import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class GuildCache {
    public static HashMap<Long, Guild> cache = new HashMap<>();
    public static void addGuild(Guild guild) {
        cache.put(guild.getIdLong(), guild);
    }
}