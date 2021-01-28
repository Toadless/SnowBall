package net.toaddev.lavalite.entities.database;

import com.mongodb.client.FindIterable;
import net.toaddev.lavalite.main.Launcher;
import org.bson.Document;

import net.toaddev.lavalite.data.Constants;
import net.toaddev.lavalite.entities.database.managers.GuildDataManager;

import java.util.HashMap;
import java.util.Objects;

public class GuildRegistry
{
    /**
     *  The cache where we store the all of the guilds prefixes.
     */
    public static HashMap<Long, String> guildRegistry = new HashMap<>();
    public static void registerGuild(long id, String prefix)
    {
        guildRegistry.put(id, prefix);
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     */
    public static void createGuild(long id)
    {
        guildRegistry.put(id, Constants.GUILD_PREFIX);
        Document guild = new Document("id", id);
        guild.append("prefix", Constants.GUILD_PREFIX);
        GuildDataManager.insert(guild);
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     * @return The {@link net.dv8tion.jda.api.entities.Guild guild} prefix.
     */
    public static String getPrefix(long id)
    {
        String prefix = guildRegistry.get(id);
        if (prefix != null)
        {
            return prefix;
        }
        Document guildDocument = new Document("id", id);
        FindIterable<Document> guild = Launcher.getDatabaseManager().getDatabase().getCollection(GuildDataManager.COLLECTION).find(guildDocument);
        if (guild.first() == null)
        {
            createGuild(id);
            return Constants.GUILD_PREFIX;
        }
        prefix = (String) Objects.requireNonNull(guild.first()).get("prefix");
        registerGuild(id, prefix);
        return prefix;
    }

    /**
     *
     * @param id The {@link net.dv8tion.jda.api.entities.Guild guild} id.
     * @param prefix The {@link net.dv8tion.jda.api.entities.Guild guild} new prefix.
     */
    public static void setPrefix(long id, String prefix)
    {
        Document newGuildDocument = new Document("id", id);
        newGuildDocument.append("prefix", prefix);
        GuildDataManager.replace(id, newGuildDocument);
        guildRegistry.remove(id);
        guildRegistry.put(id, prefix);
    }
}