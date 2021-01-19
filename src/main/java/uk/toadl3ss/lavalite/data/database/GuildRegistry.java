package uk.toadl3ss.lavalite.data.database;

import com.mongodb.client.FindIterable;
import org.bson.Document;

import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.data.database.Managers.GuildDataManager;
import uk.toadl3ss.lavalite.main.Launcher;

import java.util.HashMap;
import java.util.Objects;

public class GuildRegistry {
    private static HashMap<Long, String> guildRegistry = new HashMap<>();
    public static void registerGuild(long id, String prefix) {
        guildRegistry.put(id, prefix);
    }
    public static void createGuild(long id) {
        guildRegistry.put(id, Constants.GUILD_PREFIX);
        Document guild = new Document("id", id);
        guild.append("prefix", Constants.GUILD_PREFIX);
        GuildDataManager.insert(guild);
    }
    public static String getPrefix(long id) {
        String prefix = guildRegistry.get(id);
        if (prefix != null) {
            return prefix;
        }
        Document guildDocument = new Document("id", id);
        FindIterable<Document> guild = Launcher.getDatabaseManager().getDatabase().getCollection(GuildDataManager.COLLECTION).find(guildDocument);
        if (guild.first() == null) {
            createGuild(id);
            return Constants.GUILD_PREFIX;
        }
        prefix = (String) Objects.requireNonNull(guild.first()).get("prefix");
        registerGuild(id, prefix);
        return prefix;
    }
    public static void setPrefix(long id, String prefix) {
        Document newGuildDocument = new Document("id", id);
        newGuildDocument.append("prefix", prefix);
        GuildDataManager.replace(id, newGuildDocument);
        guildRegistry.remove(id);
        guildRegistry.put(id, prefix);
    }
}