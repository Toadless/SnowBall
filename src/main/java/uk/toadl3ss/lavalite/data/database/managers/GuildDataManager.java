package uk.toadl3ss.lavalite.data.database.managers;

import org.bson.Document;
import uk.toadl3ss.lavalite.main.Launcher;

import static com.mongodb.client.model.Filters.eq;

public class GuildDataManager {
    public static String COLLECTION = "guilds";

    public static void insert(Document object) {
        Launcher.getDatabaseManager().runTask(database -> {
            database.getCollection(COLLECTION).insertOne(object);
        });
    }

    public static void replace(long id, Document object) {
        Launcher.getDatabaseManager().runTask(database -> {
            database.getCollection(COLLECTION).replaceOne(eq("id", id), object);
        });
    }
}